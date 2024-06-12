package dev.darkokoa.fubukidaze

import dev.darkokoa.fubukidaze.core.appInitializersModule
import dev.darkokoa.fubukidaze.core.base.util.coreBaseUtilModule
import dev.darkokoa.fubukidaze.core.log.logModule
import dev.darkokoa.fubukidaze.data.db.dao.daoModule
import dev.darkokoa.fubukidaze.data.db.dbModule
import dev.darkokoa.fubukidaze.ui.screen.uiModelModule

val appModules = mutableListOf(
  coreBaseUtilModule,
  appInitializersModule,
  uiModelModule,
  dbModule,
  daoModule,
  logModule,
)