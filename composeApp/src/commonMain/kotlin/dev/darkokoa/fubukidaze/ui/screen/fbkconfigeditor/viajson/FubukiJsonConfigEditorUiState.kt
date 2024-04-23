package dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viajson

import androidx.compose.ui.text.input.TextFieldValue

data class FubukiJsonConfigEditorUiState(
  val configJsonString: TextFieldValue = TextFieldValue(),
  val configParsingFailed: Boolean = false,
  val canLaunch: Boolean = false
)