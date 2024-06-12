package dev.darkokoa.fubukidaze.data.converter

import dev.darkokoa.fubukidaze.data.model.entity.FubukidazeNodeEntity
import dev.darkokoa.fubukidaze.data.model.pojo.FubukiNodeConfig
import dev.darkokoa.fubukidaze.data.model.pojo.FubukidazeNode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun FubukidazeNode.toEntity(): FubukidazeNodeEntity {
  return FubukidazeNodeEntity().apply {
    id = this@toEntity.id
    name = this@toEntity.name
    rawConfig = this@toEntity.config.let { Json.encodeToString<FubukiNodeConfig>(it) }
  }
}

fun FubukidazeNodeEntity.toPojo(): FubukidazeNode {
  return FubukidazeNode(
    id = id,
    name = name,
    config = Json.decodeFromString(rawConfig)
  )
}