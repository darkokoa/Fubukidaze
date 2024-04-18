package dev.darkokoa.fubukidaze.core.base.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.bind
import org.koin.dsl.module

val coreBaseUtilModule = module {
  single {
    AppCoroutineDispatchers(
      io = Dispatchers.IO,
      databaseWrite = Dispatchers.IO.limitedParallelism(1),
      databaseRead = Dispatchers.IO.limitedParallelism(4),
      computation = Dispatchers.Default,
      main = Dispatchers.Main,
    )
  }

  single {
    CoroutineScope(get<AppCoroutineDispatchers>().main + SupervisorJob())
  } bind AppCoroutineScope::class
}