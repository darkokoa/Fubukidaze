package dev.darkokoa.fubukidaze.data.db

import dev.darkokoa.fubukidaze.data.model.entity.FubukidazeNodeEntity
import io.github.xilinjia.krdb.RealmConfiguration

fun buildRealmConfiguration(): RealmConfiguration {
  return RealmConfiguration
    .Builder(
      schema = setOf(
        FubukidazeNodeEntity::class,
      )
    )
    .name("fubukidaze_main_db.realm")
    .schemaVersion(1)
    .build()
}