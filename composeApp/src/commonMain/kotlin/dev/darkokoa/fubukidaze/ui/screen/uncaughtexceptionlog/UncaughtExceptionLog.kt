package dev.darkokoa.fubukidaze.ui.screen.uncaughtexceptionlog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import dev.darkokoa.fubukidaze.ui.screen.Screen

class UncaughtExceptionLog(
  private val exceptionMessage: String
) : Screen {

  @Composable
  override fun Content(navigator: Navigator, bottomSheetNavigator: BottomSheetNavigator) {

    Scaffold(
      topBar = { UncaughtExceptionLogTopBar(navigator::pop) }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .verticalScroll(rememberScrollState())
      ) {
        Text(text = exceptionMessage, style = MaterialTheme.typography.bodySmall)
      }
    }
  }
}

@Composable
private fun UncaughtExceptionLogTopBar(
  navUp: () -> Unit,
  modifier: Modifier = Modifier
) {
  TopAppBar(
    title = { Text("Uncaught Exception Log") },
    modifier = modifier,
    navigationIcon = {
      IconButton(onClick = navUp) {
        Icon(imageVector = FeatherIcons.ArrowLeft, contentDescription = null)
      }
    }
  )
}