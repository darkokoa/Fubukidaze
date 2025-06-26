@file:Suppress("PropertyName")

package dev.darkokoa.fubukidaze.data.model.entity

import io.github.xilinjia.krdb.types.RealmObject
import io.github.xilinjia.krdb.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId

class FubukidazeNodeEntity : RealmObject {
  @PrimaryKey
  var id: String = BsonObjectId().toHexString()
  var name: String? = null
  var rawConfig: String = "" // FubukiNodeConfig json string
}
