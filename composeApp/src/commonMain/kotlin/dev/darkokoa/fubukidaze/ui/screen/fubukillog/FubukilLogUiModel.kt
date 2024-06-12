package dev.darkokoa.fubukidaze.ui.screen.fubukillog

import dev.darkokoa.fubukidaze.core.UiModel
import dev.darkokoa.fubukidaze.core.log.FubukilLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class FubukilLogUiModel(
  private val fubukilLogger: FubukilLogger,
) : UiModel<FubukilLogUiState, Nothing>(FubukilLogUiState()) {

  init {
    uiModelScope.launch(Dispatchers.Default) {
      fubukilLogger.logStream().onEach { logLine ->
        intent {
          reduce { state -> state.copy(logContent = state.logContent + logLine) }
        }
      }.launchIn(this)
    }
  }
}