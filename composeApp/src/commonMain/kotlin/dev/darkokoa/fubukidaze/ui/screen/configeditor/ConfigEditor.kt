package dev.darkokoa.fubukidaze.ui.screen.configeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import compose.icons.FeatherIcons
import compose.icons.feathericons.Settings
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

    uiModel.collectSideEffect {
      when (it) {
        is ConfigEditorSideEffect.Launch -> launchFubuki(it.config)
      }
    }

    Scaffold(
      topBar = {
        ConfigEditorTopBar(
          openSettings = { navigator.push(Settings()) }
        )
      }
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

        BlankSpacer(height = 32.dp)

        Button(
          onClick = uiModel::onLaunch,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Launch")
        }

        BlankSpacer(height = 16.dp)
      }
    }
  }

  @Composable
  private fun ConfigEditorTopBar(
    openSettings: () -> Unit,
    modifier: Modifier = Modifier,
  ) {
    TopAppBar(
      title = { Text("Fubukidaze") },
      modifier = modifier,
      actions = {
        IconButton(onClick = openSettings) {
          Icon(imageVector = FeatherIcons.Settings, contentDescription = null)
        }
      }
    )
  }
}