package dev.darkokoa.fubukidaze.data.db.dao

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val daoModule = module {
  singleOf(::FubukidazeNodeDao)
}