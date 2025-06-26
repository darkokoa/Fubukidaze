package dev.darkokoa.fubukidaze.data.db

import io.github.xilinjia.krdb.Realm
import org.koin.dsl.module

val dbModule = module {
  single { Realm.open(buildRealmConfiguration()) }
}