package dev.darkokoa.fubukidaze

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import dev.darkokoa.fubukidaze.ui.screen.configeditor.ConfigEditor
import dev.darkokoa.fubukidaze.ui.theme.AppTheme

@Composable
internal fun App() = AppTheme {
  BottomSheetNavigator {
    Navigator(ConfigEditor()) {
      SlideTransition(it)
    }
  }
}

internal expect fun openUrl(url: String?)

internal expect fun launchFubuki(config: FubukiNodeConfig)