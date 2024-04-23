package dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viaparams

import androidx.compose.ui.text.input.TextFieldValue
import dev.darkokoa.fubukidaze.core.UiModel
import dev.darkokoa.fubukidaze.core.base.util.AppCoroutineDispatchers
import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import dev.darkokoa.fubukidaze.data.pojo.Group
import dev.darkokoa.fubukidaze.data.pojo.TunAddr
import kotlinx.serialization.json.Json

class FubukiParamsConfigEditorUiModel(
  private val coroutineDispatchers: AppCoroutineDispatchers
) : UiModel<FubukiParamsConfigEditorUiState, FubukiParamsConfigEditorSideEffect>(FubukiParamsConfigEditorUiState()) {

  fun onNodeNameInputChange(nodeName: TextFieldValue) = intent {
    reduce { it.copy(nodeName = nodeName) }
    reduceCanLaunch()
  }

  fun onServerIpInputChange(serverIp: TextFieldValue) = intent {
    reduce { it.copy(serverIp = serverIp) }
    reduceCanLaunch()
  }

  fun onServerPortInputChange(serverPort: TextFieldValue) = intent {
    reduce { it.copy(serverPort = serverPort) }
    reduceCanLaunch()
  }

  fun onKeyInputChange(key: TextFieldValue) = intent {
    reduce { it.copy(key = key) }
    reduceCanLaunch()
  }

  fun onTunAddrIpInputChange(tunAddrIp: TextFieldValue) = intent {
    reduce { it.copy(tunAddrIp = tunAddrIp) }
    reduceCanLaunch()
  }

  fun onTunAddrNetmaskInputChange(tunAddrNetmask: TextFieldValue) = intent {
    reduce { it.copy(tunAddrNetmask = tunAddrNetmask) }
    reduceCanLaunch()
  }

  fun onLaunch() = intent {
    val uiState = uiState()

    val nodeName = uiState.nodeName.text.trim()
    val serverAddr = uiState.serverIp.text.trim() + ":" + uiState.serverPort.text.trim()
    val key = uiState.key.text.trim()
    val tunAddrIp = uiState.tunAddrIp.text.trim()
    val tunAddrNetmask = uiState.tunAddrNetmask.text.trim()

    val fubukiNodeConfig = FubukiNodeConfig(
      groups = listOf(
        Group(
          node_name = nodeName,
          server_addr = serverAddr,
          key = key,
          tun_addr = TunAddr(tunAddrIp, tunAddrNetmask)
        )
      )
    )

//    postSideEffect(ConfigEditorSideEffect.SnackbarMessage("Launching..."))
    postSideEffect(FubukiParamsConfigEditorSideEffect.Launch(fubukiNodeConfig))
  }

  private fun reduceCanLaunch() = intent {
    val uiState = uiState()
    val canLaunch = uiState.nodeName.text.trim().isNotEmpty()
      && uiState.serverIp.text.trim().isNotEmpty()
      && uiState.serverPort.text.trim().isNotEmpty()
      && uiState.key.text.trim().isNotEmpty()
      && uiState.tunAddrIp.text.trim().isNotEmpty()
      && uiState.tunAddrNetmask.text.trim().isNotEmpty()

    reduce { it.copy(canLaunch = canLaunch) }
  }
}