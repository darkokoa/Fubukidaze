package dev.darkokoa.fubukidaze.ui.screen.configeditor

import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.darkokoa.fubukidaze.core.UiModel
import dev.darkokoa.fubukidaze.core.base.util.AppCoroutineDispatchers
import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import dev.darkokoa.fubukidaze.data.pojo.Group
import dev.darkokoa.fubukidaze.data.pojo.TunAddr
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConfigEditorUiModel(
  private val coroutineDispatchers: AppCoroutineDispatchers
) : UiModel<ConfigEditorUiState, ConfigEditorSideEffect>(ConfigEditorUiState()) {

  fun onNodeNameInputChange(nodeName: TextFieldValue) = intent {
    mutableState.update { it.copy(nodeName = nodeName) }
  }

  fun onServerIpInputChange(serverIp: TextFieldValue) = intent {
    reduce { it.copy(serverIp = serverIp) }
  }

  fun onServerPortInputChange(serverPort: TextFieldValue) = intent {
    reduce { it.copy(serverPort = serverPort) }
  }

  fun onKeyInputChange(key: TextFieldValue) = intent {
    reduce { it.copy(key = key) }
  }

  fun onTunAddrIpInputChange(tunAddrIp: TextFieldValue) = intent {
    reduce { it.copy(tunAddrIp = tunAddrIp) }
  }

  fun onTunAddrNetmaskInputChange(tunAddrNetmask: TextFieldValue) = intent {
    reduce { it.copy(tunAddrNetmask = tunAddrNetmask) }
  }

  fun onLaunch() = intent {
    val uiState = uiState()

    val nodeName = uiState.nodeName.text.trim()
    val serverAddr = uiState.serverIp.text.trim() + ":" + uiState.serverPort.text.trim()
    val key = uiState.key.text.trim()
    val tunAddrIp = uiState.tunAddrIp.text.trim()
    val tunAddrNetmask = uiState.tunAddrNetmask.text.trim()

    val fubukiNodeConfig = FubukiNodeConfig(
      listOf(
        Group(
          node_name = nodeName,
          server_addr = serverAddr,
          key = key,
          tun_addr = TunAddr(tunAddrIp, tunAddrNetmask)
        )
      )
    )

    postSideEffect(ConfigEditorSideEffect.Launch(fubukiNodeConfig))
  }
}