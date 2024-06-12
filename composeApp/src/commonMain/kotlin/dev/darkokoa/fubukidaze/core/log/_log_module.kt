package dev.darkokoa.fubukidaze.core.log

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logModule = module {
  singleOf(::FubukilLogger)
}