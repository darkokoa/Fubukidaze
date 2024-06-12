package dev.darkokoa.fubukidaze.data.db

import io.realm.kotlin.Realm
import org.koin.dsl.module

val dbModule = module {
  single { Realm.open(buildRealmConfiguration()) }
}