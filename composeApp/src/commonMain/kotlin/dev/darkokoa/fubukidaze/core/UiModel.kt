package dev.darkokoa.fubukidaze.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.darkokoa.fubukidaze.core.base.util.AppCoroutineDispatchers
import dev.darkokoa.fubukidaze.core.flow.EventFlow
import dev.darkokoa.fubukidaze.core.flow.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class UiModel<UI_STATE, SIDE_EFFECT>(initialState: UI_STATE) : StateScreenModel<UI_STATE>(initialState),
  KoinComponent {

  private val coroutineDispatchers: AppCoroutineDispatchers by inject()

  private val sideEffectFlow = EventFlow<SIDE_EFFECT>()

  fun intent(intentFn: suspend CoroutineScope.() -> Unit) = screenModelScope.launch(coroutineDispatchers.computation) {
    intentFn()
  }

  fun reduce(reduceFn: (UI_STATE) -> UI_STATE) {
    mutableState.update { reduceFn(it) }
  }

  val uiState get() = mutableState.value

  suspend fun uiState() = mutableState.first()

  @Composable
  fun collectAsState() = state.collectAsState()

  suspend fun postSideEffect(sideEffectAction: SIDE_EFFECT) {
    sideEffectFlow.emit(sideEffectAction)
  }

  @Composable
  fun collectSideEffect(sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit)) {
    LaunchedEffect(sideEffectFlow) {
      sideEffectFlow.collect {
        sideEffect(it)
      }
    }
  }
}