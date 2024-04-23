package dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viajson

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import dev.darkokoa.fubukidaze.launchFubuki
import dev.darkokoa.fubukidaze.ui.BlankSpacer
import dev.darkokoa.fubukidaze.ui.screen.Screen

class FubukiJsonConfigEditor : Screen {

  @Composable
  override fun Content(
    navigator: Navigator,
    bottomSheetNavigator: BottomSheetNavigator
  ) {

    val uiModel = getScreenModel<FubukiJsonConfigEditorUiModel>()
    val uiState by uiModel.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val imeController = LocalSoftwareKeyboardController.current
    val configTextFieldFocusRequester = remember { FocusRequester() }


    uiModel.collectSideEffect {
      when (it) {
        is FubukiJsonConfigEditorSideEffect.Launch -> launchFubuki(it.config)
        is FubukiJsonConfigEditorSideEffect.SnackbarMessage -> snackbarHostState.showSnackbar(it.message)
      }
    }

    LifecycleEffect(
      onDisposed = {
        runCatching {
          configTextFieldFocusRequester.freeFocus()
        }
        imeController?.hide()
      }
    )

    LaunchedEffect(Unit) {
      configTextFieldFocusRequester.requestFocus()
    }

    ConfigEditorContent(
      uiState = uiState,
      snackbarHostState = snackbarHostState,
      configTextFieldFocusRequester = configTextFieldFocusRequester,
      onLaunch = uiModel::onLaunch,
      onConfigInputChange = uiModel::onConfigInputChange
    )
  }
}

@Composable
private fun ConfigEditorContent(
  uiState: FubukiJsonConfigEditorUiState,
  snackbarHostState: SnackbarHostState,
  configTextFieldFocusRequester: FocusRequester,
  onLaunch: () -> Unit,
  onConfigInputChange: (TextFieldValue) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      ConfigEditorTopBar(
        onLaunch = onLaunch,
        canLaunch = uiState.canLaunch
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    modifier = modifier.imePadding()
  ) { paddingValues ->
    Column(
      modifier = Modifier.fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      BlankSpacer(16.dp)

      OutlinedTextField(
        value = uiState.configJsonString,
        onValueChange = onConfigInputChange,
        modifier = Modifier.fillMaxWidth().focusRequester(configTextFieldFocusRequester),
        isError = uiState.configParsingFailed,
        label = { Text("Config") },
        minLines = 10,
      )

      BlankSpacer(16.dp)
    }
  }
}

@Composable
private fun ConfigEditorTopBar(
  onLaunch: () -> Unit,
  canLaunch: Boolean,
  modifier: Modifier = Modifier,
) {
  TopAppBar(
    title = { Text("Fubukidaze") },
    modifier = modifier,
    actions = {
      Button(onClick = onLaunch, enabled = canLaunch) { Text("launch") }

      BlankSpacer(8.dp)
    }
  )
}
