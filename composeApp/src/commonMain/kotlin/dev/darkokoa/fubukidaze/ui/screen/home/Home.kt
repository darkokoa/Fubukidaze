package dev.darkokoa.fubukidaze.ui.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import compose.icons.FeatherIcons
import compose.icons.feathericons.Pause
import compose.icons.feathericons.Play
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Terminal
import dev.darkokoa.fubukidaze.data.model.pojo.FubukidazeNode
import dev.darkokoa.fubukidaze.launchFubuki
import dev.darkokoa.fubukidaze.switchFubukiConnection
import dev.darkokoa.fubukidaze.terminateFubuki
import dev.darkokoa.fubukidaze.ui.component.FubukidazeNodeItem
import dev.darkokoa.fubukidaze.ui.screen.Screen
import dev.darkokoa.fubukidaze.ui.screen.nodeeditor.NodeEditor
import dev.darkokoa.fubukidaze.ui.screen.fubukillog.FubukilLog

class Home : Screen {

  @Composable
  override fun Content(
    navigator: Navigator,
    bottomSheetNavigator: BottomSheetNavigator
  ) {
    val uiModel = koinScreenModel<HomeUiModel>()
    val uiState by uiModel.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    uiModel.collectSideEffect {
      when (it) {
        is HomeSideEffect.LaunchFubuki -> launchFubuki(it.config)
        is HomeSideEffect.SwitchFubukiConnection -> switchFubukiConnection(it.config)
        HomeSideEffect.TerminateFubuki -> terminateFubuki()
        is HomeSideEffect.SnackbarMessage -> snackbarHostState.showSnackbar(it.message)
      }
    }

    HomeContent(
      navigator = navigator,
      bottomSheetNavigator = bottomSheetNavigator,
      snackbarHostState = snackbarHostState,
      uiState = uiState,
      onSelect = uiModel::onSelectChange,
      onLaunchSwitch = uiModel::onLaunchClick
    )
  }
}

@Composable
private fun HomeContent(
  navigator: Navigator,
  bottomSheetNavigator: BottomSheetNavigator,
  snackbarHostState: SnackbarHostState,
  uiState: HomeUiState,
  onSelect: (node: FubukidazeNode) -> Unit,
  onLaunchSwitch: () -> Unit,
  modifier: Modifier = Modifier
) {
  val topBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  Scaffold(
    topBar = {
      HomeTopBar(
        openConfigEditor = { navigator.push(NodeEditor()) },
        scrollBehavior = topBarScrollBehavior
      )
    },
    bottomBar = {
      HomeBottomBar(
        hasLaunch = uiState.hasLaunchFubuki,
        onLaunchSwitch = onLaunchSwitch,
        openRuntimeLog = { navigator.push(FubukilLog()) }
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    modifier = modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
  ) { paddingsValues ->

    if (uiState.loading) {
      Box(
        modifier = Modifier.padding(paddingsValues).fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator()
      }
    } else {
      LazyColumn(modifier = Modifier.padding(paddingsValues).fillMaxSize()) {
        items(items = uiState.fubukidazeNodeList, key = { it.id }) { node ->
          FubukidazeNodeItem(
            node = node,
            isSelect = node.id == uiState.selectedNodeId,
            onItemClick = { onSelect(node) },
            onEditClick = {
              navigator.push(
                NodeEditor(NodeEditor.Mode.Edit(it.id))
              )
            },
            modifier = Modifier.fillMaxWidth().animateItem()
          )
        }
      }
    }
  }
}

@Composable
private fun HomeTopBar(
  openConfigEditor: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
) {
  LargeTopAppBar(
    title = { Text(text = "Fubukidaze") },
    modifier = modifier,
    actions = {
      IconButton(onClick = openConfigEditor) {
        Icon(imageVector = FeatherIcons.Plus, contentDescription = "add config")
      }
    },
    scrollBehavior = scrollBehavior
  )
}

@Composable
private fun HomeBottomBar(
  hasLaunch: Boolean,
  onLaunchSwitch: () -> Unit,
  openRuntimeLog: () -> Unit,
  modifier: Modifier = Modifier
) {
  BottomAppBar(
    modifier = modifier,
    actions = {
      IconButton(onClick = { openRuntimeLog() }) {
        Icon(imageVector = FeatherIcons.Terminal, contentDescription = null)
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = onLaunchSwitch,
        containerColor = if (hasLaunch) MaterialTheme.colorScheme.tertiaryContainer else BottomAppBarDefaults.bottomAppBarFabColor,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
      ) {
        Crossfade(targetState = hasLaunch) {
          val switchIcon = if (it) FeatherIcons.Pause else FeatherIcons.Play
          Icon(imageVector = switchIcon, null, modifier = Modifier.size(18.dp))
        }
      }
    }
  )
}
