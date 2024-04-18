package dev.darkokoa.fubukidaze

import dev.darkokoa.fubukidaze.core.appInitializersModule
import dev.darkokoa.fubukidaze.core.base.util.coreBaseUtilModule
import dev.darkokoa.fubukidaze.ui.screen.uiModelModule

val appModules = listOf(
  coreBaseUtilModule,
  appInitializersModule,
  uiModelModule
)