package dev.darkokoa.fubukidaze.android

import dev.darkokoa.fubukidaze.android.notification.FubukidazeNotification
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidModule = module {
  singleOf(::FubukidazeNotification)
}