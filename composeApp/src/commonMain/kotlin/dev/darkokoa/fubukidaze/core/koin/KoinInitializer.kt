package dev.darkokoa.fubukidaze.core.koin

import dev.darkokoa.fubukidaze.appModules
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object KoinInitializer {
  fun initialize(
    platformModules: List<Module> = emptyList(),
    additionalBlock: (KoinApplication.() -> Unit)? = null
  ) {
    startKoin {
//      logger(KermitKoinLogger(Logger.withTag("koin")))
      modules(appModules.apply {
        if (platformModules.isNotEmpty()) addAll(platformModules)
      })
      additionalBlock?.invoke(this)
    }
  }
}