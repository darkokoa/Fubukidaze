package dev.darkokoa.fubukidaze.ui.screen.fubukillog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import dev.darkokoa.fubukidaze.ui.screen.Screen
import kotlin.math.abs

class FubukilLog : Screen {

  @Composable
  override fun Content(navigator: Navigator, bottomSheetNavigator: BottomSheetNavigator) {
    val uiModel = koinScreenModel<FubukilLogUiModel>()
    val uiState by uiModel.collectAsState()

    FubukilLogContent(navigator, uiState)
  }

}

@Composable
private fun FubukilLogContent(
  navigator: Navigator,
  uiState: FubukilLogUiState,
) {
  val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  Scaffold(
    topBar = { FubukilLogTopBar(navigator::pop, topBarScrollBehavior) },
    modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection)
  ) { paddingValues ->

    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    val logTextStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontSize = 13.sp)

    LaunchedEffect(uiState.logContent) {
      val scrollStateValueSp = with(density) { scrollState.value.toSp() }
      val scrollStateMaxValueSp = with(density) { scrollState.maxValue.toSp() }

      println("scrollState.value.toSp: ${with(density) { scrollState.value.toSp() }}")
      println("scrollState.maxValue.toSp: ${with(density) { scrollState.maxValue.toSp() }}")
      println("scrollState.logLineHeightSp: ${logTextStyle.lineHeight}")

      if (abs(scrollStateValueSp.value - scrollStateMaxValueSp.value) <= logTextStyle.lineHeight.value + 4) {
        scrollState.scrollTo(Int.MAX_VALUE)
      }
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(scrollState)
    ) {
      Markdown(
        content = buildString {
          appendLine("```")
          appendLine(uiState.logContent.trimEnd())
          appendLine("```")
        },
        colors = markdownColor(),
        typography = markdownTypography(code = logTextStyle)
      )
    }
  }
}

@Composable
private fun FubukilLogTopBar(
  navUp: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier
) {
  TopAppBar(
    title = { Text("Fubukil Log") },
    modifier = modifier,
    navigationIcon = {
      IconButton(onClick = navUp) {
        Icon(imageVector = FeatherIcons.ArrowLeft, contentDescription = null)
      }
    },
    scrollBehavior = scrollBehavior,
  )
}