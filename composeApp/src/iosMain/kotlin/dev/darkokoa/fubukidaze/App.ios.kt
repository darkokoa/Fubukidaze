package dev.darkokoa.fubukidaze

import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal actual fun openUrl(url: String?) {
  val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
  UIApplication.sharedApplication.openURL(nsUrl)
}

internal actual fun launchFubuki(config: FubukiNodeConfig) {
}