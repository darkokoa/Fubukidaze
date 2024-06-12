package dev.darkokoa.fubukidaze.ui.screen.nodeeditor

import androidx.compose.ui.text.input.TextFieldValue

data class NodeEditorUiState(
  val processing: Boolean = false,
  val nodeName: TextFieldValue = TextFieldValue(),
  val configJsonString: TextFieldValue = TextFieldValue(),
  val configParsingFailed: Boolean = false,
  val canSave: Boolean = false,
)