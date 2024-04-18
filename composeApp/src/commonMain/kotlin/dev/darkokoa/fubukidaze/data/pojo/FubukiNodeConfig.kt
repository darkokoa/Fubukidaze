package dev.darkokoa.fubukidaze.data.pojo

import kotlinx.serialization.Serializable

@Serializable
data class FubukiNodeConfig(
  val groups: List<Group>
)

@Suppress("PropertyName")
@Serializable
data class Group(
  val node_name: String,
  val server_addr: String,
  val key: String,
  val tun_addr: TunAddr
)

@Serializable
data class TunAddr(
  val ip: String,
  val netmask: String
)