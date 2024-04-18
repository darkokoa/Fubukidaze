package dev.darkokoa.fubukidaze

import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import java.awt.Desktop
import java.net.URI

internal actual fun openUrl(url: String?) {
  val uri = url?.let { URI.create(it) } ?: return
  Desktop.getDesktop().browse(uri)
}

internal actual fun launchFubuki(config: FubukiNodeConfig) {
}