package dev.darkokoa.fubukidaze.ui.screen.configeditor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import compose.icons.FeatherIcons
import compose.icons.feathericons.Edit
import dev.darkokoa.fubukidaze.launchFubuki
import dev.darkokoa.fubukidaze.ui.BlankSpacer
import dev.darkokoa.fubukidaze.ui.screen.Screen
import dev.darkokoa.fubukidaze.ui.screen.settings.Settings

class ConfigEditor : Screen {

  @Composable
  override fun Content(
    navigator: Navigator,
    bottomSheetNavigator: BottomSheetNavigator
  ) {

    val uiModel = getScreenModel<ConfigEditorUiModel>()
    val uiState by uiModel.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val imeController = LocalSoftwareKeyboardController.current

    uiModel.collectSideEffect {
      when (it) {
        is ConfigEditorSideEffect.Launch -> launchFubuki(it.config)
        is ConfigEditorSideEffect.SnackbarMessage -> snackbarHostState.showSnackbar(it.message)
      }
    }

    LifecycleEffect(
      onDisposed = { imeController?.hide() }
    )

    if (uiState.showConfigInputDialog) {
      ConfigInputDialog(
        onDismiss = uiModel::onDismissConfigInputDialog,
        onInputConfirm = uiModel::onAutofillViaJson
      )
    }

    Scaffold(
      topBar = {
        ConfigEditorTopBar(
          openSettings = { navigator.push(Settings()) },
          onLaunch = uiModel::onLaunch,
          onShowConfigInputDialog = {
            imeController?.hide()
            uiModel.onShowConfigInputDialog()
          },
          canLaunch = uiState.canLaunch
        )
      },
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      modifier = Modifier.imePadding()
    ) { paddingValues ->
      Column(
        modifier = Modifier.fillMaxSize()
          .padding(paddingValues)
          .padding(horizontal = 16.dp)
          .verticalScroll(rememberScrollState()),
      ) {
        BlankSpacer(height = 16.dp)

        OutlinedTextField(
          value = uiState.nodeName,
          onValueChange = uiModel::onNodeNameInputChange,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("node name") }
        )

        BlankSpacer(height = 16.dp)

        OutlinedTextField(
          value = uiState.serverIp,
          onValueChange = uiModel::onServerIpInputChange,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("server ip") }
        )

        BlankSpacer(height = 16.dp)

        OutlinedTextField(
          value = uiState.serverPort,
          onValueChange = uiModel::onServerPortInputChange,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("server port") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        BlankSpacer(height = 16.dp)

        OutlinedTextField(
          value = uiState.key,
          onValueChange = uiModel::onKeyInputChange,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("key") }
        )

        BlankSpacer(height = 16.dp)

        OutlinedTextField(
          value = uiState.tunAddrIp,
          onValueChange = uiModel::onTunAddrIpInputChange,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("tun addr ip") }
        )

        BlankSpacer(height = 16.dp)

        OutlinedTextField(
          value = uiState.tunAddrNetmask,
          onValueChange = uiModel::onTunAddrNetmaskInputChange,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("tun addr netmask") }
        )

        BlankSpacer(height = 16.dp)
      }
    }
  }

  @Composable
  private fun ConfigEditorTopBar(
    openSettings: () -> Unit,
    onLaunch: () -> Unit,
    onShowConfigInputDialog: () -> Unit,
    canLaunch: Boolean,
    modifier: Modifier = Modifier,
  ) {
    TopAppBar(
      title = { Text("Fubukidaze") },
      modifier = modifier,
      actions = {
//        IconButton(onClick = openSettings) {
//          Icon(imageVector = FeatherIcons.Settings, contentDescription = null)
//        }

        IconButton(onClick = onShowConfigInputDialog) {
          Icon(imageVector = FeatherIcons.Edit, contentDescription = null)
        }

        Button(onClick = onLaunch, enabled = canLaunch) { Text("launch") }

        Spacer(Modifier.width(8.dp))
      }
    )
  }

  @Composable
  private fun ConfigInputDialog(
    onDismiss: () -> Unit,
    onInputConfirm: (String) -> Unit
  ) {
    val requester = remember { FocusRequester() }
    var input by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(Unit) {
      requester.requestFocus()
    }

    AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
        TextButton(onClick = {
          onInputConfirm(input.text)
          onDismiss()
        }) {
          Text("Confirm")
        }
      },
      title = {
        Text("Autofill via JSON")
      },
      text = {
        OutlinedTextField(
          value = input,
          onValueChange = { input = it },
          modifier = Modifier.focusRequester(requester),
          label = {
            Text("fubuki config")
          },
          minLines = 8
        )
      }
    )
  }
}