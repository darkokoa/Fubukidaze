package dev.darkokoa.fubukidaze.core

import dev.darkokoa.fubukidaze.core.base.appinitializer.AppInitializer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appInitializersModule = module {
  singleOf(::AppInitializers)
}

class AppInitializers(

) : AppInitializer {
  override fun initialize() {

  }
}