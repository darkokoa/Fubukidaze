package dev.darkokoa.fubukidaze.data.db.dao

import dev.darkokoa.fubukidaze.data.model.entity.FubukidazeNodeEntity
import io.github.xilinjia.krdb.Realm
import io.github.xilinjia.krdb.UpdatePolicy
import io.github.xilinjia.krdb.ext.query
import io.github.xilinjia.krdb.notifications.ResultsChange
import io.github.xilinjia.krdb.query.RealmSingleQuery
import kotlinx.coroutines.flow.Flow

class FubukidazeNodeDao(
  private val realm: Realm
) {

  fun allNodesFlow(): Flow<ResultsChange<FubukidazeNodeEntity>> {
    return realm.query<FubukidazeNodeEntity>().find().asFlow()
  }


  suspend fun upsert(node: FubukidazeNodeEntity) {
    realm.write {
      val oldNode = query<FubukidazeNodeEntity>("id == $0", node.id).first().find()
      if (oldNode == null) {
        copyToRealm(node)
      } else {
        copyToRealm(oldNode.apply {
          name = node.name
          rawConfig = node.rawConfig
        }, UpdatePolicy.ALL)
      }
    }
  }

  fun findById(id: String): RealmSingleQuery<FubukidazeNodeEntity> {
    return realm.query<FubukidazeNodeEntity>("id == $0", id).first()
  }

  suspend fun deleteById(id: String) {
    realm.write {
      val single = query<FubukidazeNodeEntity>("id == $0", id).first()
      delete(single)
    }
  }
}