package dev.darkokoa.fubukidaze.ui.screen.configeditor

import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig

sealed interface ConfigEditorSideEffect {
  data class Launch(val config: FubukiNodeConfig) : ConfigEditorSideEffect
}