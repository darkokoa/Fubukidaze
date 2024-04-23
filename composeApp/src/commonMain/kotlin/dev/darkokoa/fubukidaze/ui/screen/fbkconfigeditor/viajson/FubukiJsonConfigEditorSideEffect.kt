package dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viajson

import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig

sealed interface FubukiJsonConfigEditorSideEffect {
  data class Launch(val config: FubukiNodeConfig) : FubukiJsonConfigEditorSideEffect
  data class SnackbarMessage(val message: String) : FubukiJsonConfigEditorSideEffect
}