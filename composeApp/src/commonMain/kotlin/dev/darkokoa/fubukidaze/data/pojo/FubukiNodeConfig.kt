@file:Suppress("PropertyName")

package dev.darkokoa.fubukidaze.data.pojo

import kotlinx.serialization.Serializable

@Serializable
data class FubukiNodeConfig(
  val mtu: Int = 1446,
  val channel_limit: Int? = null,
  val api_addr: String? = null,
  val tcp_heartbeat_interval_secs: Int? = null,
  val udp_heartbeat_interval_secs: Int? = null,
  val tcp_heartbeat_continuous_loss: Int? = null,
  val udp_heartbeat_continuous_loss: Int? = null,
  val udp_heartbeat_continuous_recv: Int? = null,
  val reconnect_interval_secs: Int? = null,
  val udp_socket_recv_buffer_size: Int? = null,
  val udp_socket_send_buffer_size: Int? = null,
  val groups: List<Group> = emptyList(),
  val features: Features? = null,
)

@Serializable
data class Group(
  val node_name: String? = null,
  val server_addr: String,
  val tun_addr: TunAddr? = null,
  val key: String? = null,
  val enable_key_rotation: Boolean? = null,
  val mode: Mode? = null,
  val specify_mode: String? = null, // JsonElement?
  val lan_ip_addr: String? = null,
  val allowed_ips: List<String>? = null,
  val ips: String? = null, // JsonElement?
  val allow_packet_forward: Boolean? = null,
  val allow_packet_not_in_rules_send_to_kernel: Boolean? = null,
  val socket_bind_device: String? = null,
)

@Serializable
data class Mode(
  val p2p: List<String>,
  val relay: List<String>
)

@Serializable
data class TunAddr(
  val ip: String,
  val netmask: String
)

@Serializable
data class Features(
  val disable_api_server: Boolean? = null,
  val disable_hosts_operation: Boolean? = null,
  val disable_signal_handling: Boolean? = null,
  val disable_route_operation: Boolean? = null,
)
