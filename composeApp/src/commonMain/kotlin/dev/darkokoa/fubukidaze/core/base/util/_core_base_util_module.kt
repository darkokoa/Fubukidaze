package dev.darkokoa.fubukidaze.core.base.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.bind
import org.koin.dsl.module

val coreBaseUtilModule = module {
  single {
    CoroutineScope(Dispatchers.Main + SupervisorJob())
  } bind AppCoroutineScope::class
}