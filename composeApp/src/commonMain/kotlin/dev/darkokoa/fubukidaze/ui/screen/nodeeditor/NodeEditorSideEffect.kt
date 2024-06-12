package dev.darkokoa.fubukidaze.ui.screen.nodeeditor

sealed interface NodeEditorSideEffect {
  data class SnackbarMessage(val message: String) : NodeEditorSideEffect
  data object NavUp: NodeEditorSideEffect
}