package dev.darkokoa.fubukidaze.core.koin

import dev.darkokoa.fubukidaze.appModules
import org.koin.core.context.startKoin

object KoinInitializer {
  fun initialize() {
    startKoin {
//      logger(KermitKoinLogger(Logger.withTag("koin")))
      modules(appModules)
    }
  }
}