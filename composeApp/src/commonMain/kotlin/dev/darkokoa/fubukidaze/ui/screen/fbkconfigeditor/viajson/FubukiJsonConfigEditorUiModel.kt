package dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viajson

import androidx.compose.ui.text.input.TextFieldValue
import dev.darkokoa.fubukidaze.core.UiModel
import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import kotlinx.serialization.json.Json

class FubukiJsonConfigEditorUiModel :
  UiModel<FubukiJsonConfigEditorUiState, FubukiJsonConfigEditorSideEffect>(FubukiJsonConfigEditorUiState()) {

  fun onConfigInputChange(configJsonString: TextFieldValue) = intent {
    reduce {
      it.copy(
        configJsonString = configJsonString,
        canLaunch = configJsonString.text.trim().isNotEmpty(),
        configParsingFailed = false
      )
    }
  }

  fun onLaunch() = intent {
    val jsonString = uiState().configJsonString.text.trim()

    runCatching {
      Json.decodeFromString<FubukiNodeConfig>(jsonString)
    }.onSuccess {
      postSideEffect(FubukiJsonConfigEditorSideEffect.Launch(it))
    }.onFailure { throwable ->
      reduce { it.copy(configParsingFailed = true) }
      postSideEffect(FubukiJsonConfigEditorSideEffect.SnackbarMessage("Config parsing failed ❌"))

      throwable.printStackTrace()
    }

  }
}