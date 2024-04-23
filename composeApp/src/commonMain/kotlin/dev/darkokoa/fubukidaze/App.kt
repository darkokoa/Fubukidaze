package dev.darkokoa.fubukidaze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viajson.FubukiJsonConfigEditor
import dev.darkokoa.fubukidaze.ui.theme.AppTheme

@Composable
internal fun App() = AppTheme {
  BottomSheetNavigator {
    Navigator(FubukiJsonConfigEditor()) {
      GlobalNavigator.register(it, rememberCoroutineScope())

      SlideTransition(it)
    }
  }
}

internal expect fun openUrl(url: String?)

internal expect fun launchFubuki(config: FubukiNodeConfig)