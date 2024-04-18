package dev.darkokoa.fubukidaze.ui.screen.configeditor

import androidx.compose.ui.text.input.TextFieldValue

data class ConfigEditorUiState(
  val nodeName: TextFieldValue = TextFieldValue(),
  val serverIp: TextFieldValue = TextFieldValue(),
  val serverPort: TextFieldValue = TextFieldValue(),
  val key: TextFieldValue = TextFieldValue(),
  val tunAddrIp: TextFieldValue = TextFieldValue(),
  val tunAddrNetmask: TextFieldValue = TextFieldValue(),
)
