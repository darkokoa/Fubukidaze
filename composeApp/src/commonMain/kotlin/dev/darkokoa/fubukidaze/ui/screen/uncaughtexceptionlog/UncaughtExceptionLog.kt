package dev.darkokoa.fubukidaze.ui.screen.uncaughtexceptionlog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import dev.darkokoa.fubukidaze.ui.screen.Screen

class UncaughtExceptionLog(
  private val exceptionMessage: String
) : Screen {

  @Composable
  override fun Content(navigator: Navigator, bottomSheetNavigator: BottomSheetNavigator) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
      topBar = { UncaughtExceptionLogTopBar(navigator::pop, topBarScrollBehavior) },
      modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .verticalScroll(rememberScrollState())
      ) {

        val logTextStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)

        Markdown(
          content = buildString {
            appendLine("```")
            appendLine(exceptionMessage)
            appendLine("```")
          },
          colors = markdownColor(),
          typography = markdownTypography(code = logTextStyle)
        )
      }
    }
  }
}

@Composable
private fun UncaughtExceptionLogTopBar(
  navUp: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier
) {
  TopAppBar(
    title = { Text("Uncaught Exception Log") },
    modifier = modifier,
    navigationIcon = {
      IconButton(onClick = navUp) {
        Icon(imageVector = FeatherIcons.ArrowLeft, contentDescription = null)
      }
    },
    scrollBehavior = scrollBehavior
  )
}