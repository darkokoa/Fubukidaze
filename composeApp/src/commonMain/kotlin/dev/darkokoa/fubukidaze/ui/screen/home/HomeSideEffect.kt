package dev.darkokoa.fubukidaze.ui.screen.home

import dev.darkokoa.fubukidaze.data.model.pojo.FubukiNodeConfig

sealed interface HomeSideEffect {
  data class LaunchFubuki(val config: FubukiNodeConfig) : HomeSideEffect
  data object TerminateFubuki : HomeSideEffect
  data class SwitchFubukiConnection(val config: FubukiNodeConfig) : HomeSideEffect
  data class SnackbarMessage(val message: String) : HomeSideEffect
}