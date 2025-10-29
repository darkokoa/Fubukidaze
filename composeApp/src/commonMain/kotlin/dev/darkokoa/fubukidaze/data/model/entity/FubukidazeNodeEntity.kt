@file:Suppress("PropertyName")

package dev.darkokoa.fubukidaze.data.model.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId

class FubukidazeNodeEntity : RealmObject {
  @PrimaryKey
  var id: String = BsonObjectId().toHexString()
  var name: String? = null
  var rawConfig: String = "" // FubukiNodeConfig json string
}
