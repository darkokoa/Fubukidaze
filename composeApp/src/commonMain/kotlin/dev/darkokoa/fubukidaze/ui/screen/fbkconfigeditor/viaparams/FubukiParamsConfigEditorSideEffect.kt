package dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viaparams

import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig

sealed interface FubukiParamsConfigEditorSideEffect {
  data class Launch(val config: FubukiNodeConfig) : FubukiParamsConfigEditorSideEffect
  data class SnackbarMessage(val message: String) : FubukiParamsConfigEditorSideEffect
}