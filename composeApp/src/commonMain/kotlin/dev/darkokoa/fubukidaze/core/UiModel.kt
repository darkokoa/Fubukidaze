package dev.darkokoa.fubukidaze.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class UiModel<UI_STATE, SIDE_EFFECT>(initialState: UI_STATE) : StateScreenModel<UI_STATE>(initialState),
  KoinComponent {

  private val sideEffectChannel = Channel<SIDE_EFFECT>(Channel.BUFFERED)

  val sideEffectFlow = sideEffectChannel.receiveAsFlow()

  val uiModelScope get() = screenModelScope

  fun intent(intentBlock: suspend CoroutineScope.() -> Unit) =
    uiModelScope.launch(Dispatchers.Default) {
      intentBlock()
    }

  suspend fun reduce(reduceBlock: suspend (UI_STATE) -> UI_STATE) {
    mutableState.update { reduceBlock(it) }
  }

  val uiState get() = mutableState.value

  suspend fun uiState() = mutableState.first()

  @Composable
  fun collectAsState() = state.collectAsState()

  suspend fun postSideEffect(sideEffectAction: SIDE_EFFECT) {
    sideEffectChannel.send(sideEffectAction)
  }

  @Composable
  fun collectSideEffect(sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit)) {
    LaunchedEffect(sideEffectFlow) {
      sideEffectChannel.receiveAsFlow().collect {
        sideEffect(it)
      }
    }
  }
}