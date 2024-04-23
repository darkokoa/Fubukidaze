package dev.darkokoa.fubukidaze.android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import dev.darkokoa.fubukidaze.android.util.fubuki.FubukiJNA
import dev.darkokoa.fubukidaze.core.base.util.AppCoroutineDispatchers
import dev.darkokoa.fubukidaze.core.base.util.AppCoroutineScope
import dev.darkokoa.fubukidaze.core.base.util.netmaskToPrefixLength
import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FubukiVpnService : VpnService(), KoinComponent {

  private val appCoroutineScope: AppCoroutineScope by inject()
  private val appCoroutineDispatchers: AppCoroutineDispatchers by inject()

  private val fubuki: FubukiJNA = FubukiJNA.INSTANCE

  private var isRunning = false
  private var pfd: ParcelFileDescriptor? = null
  private var fubukiHandle: FubukiJNA.FubukiHandle? = null


  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    setup(intent)

    val channelId = createNotificationChannel()

    val nb = NotificationCompat.Builder(this, channelId)
      .setContentTitle("Fubuki VPN Service")
      .setContentText("lalalala")
      .setPriority(NotificationCompat.PRIORITY_MAX)

    startForeground(FUBUKI_VPN_SERVICE_NOTIFICATION_ID, nb.build())

    return START_STICKY
  }

  override fun onRevoke() {
    stopFubuki()
  }

  private fun setup(intent: Intent?) {
    println("$TAG setup(intent: $intent)")

    intent ?: return
    val configJsonString = intent.getStringExtra(ARG_FUBUKI_NODE_CONFIG) ?: return

    println("$TAG configJsonString: $configJsonString")

    val prepare = prepare(this)
    if (prepare != null) {
      println("$TAG prepare != null")
      return
    }

    val config = Json.decodeFromString<FubukiNodeConfig>(configJsonString)

    val builder = Builder().apply {
      setMtu(config.mtu)
      config.groups.forEach { group ->
        group.tun_addr ?: return@forEach

        val addrIp = group.tun_addr.ip
        val addrNetmask = group.tun_addr.netmask
        addAddress(addrIp, netmaskToPrefixLength(addrNetmask))
      }
//      addRoute("0.0.0.0", 0)
      addDnsServer(FUBUKI_DEFAULT_DNS_SERVER)
//      setSession(config.api_addr ?: "")
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        setMetered(false)
      }
    }

    try {
      pfd?.close()
    } catch (ignored: Exception) {
      ignored.printStackTrace()
      // ignored
    }

    try {
      val _pfd = builder.establish() ?: return

      println("$TAG mInterface = builder.establish()")

      pfd = _pfd
      isRunning = true

      val fubukiStartOptions = FubukiJNA.FubukiStartOptions().apply {
        node_config_json = configJsonString
        tun_fd = _pfd.fd
      }

      fubukiHandle = fubuki.fubuki_start(fubukiStartOptions)

    } catch (e: Exception) {
      e.printStackTrace()
      stopFubuki()
    }
  }

  private fun stopFubuki(isForced: Boolean = true) {

    isRunning = false

    try {
      fubukiHandle?.let(fubuki::fubuki_stop)
    } catch (e: Exception) {
      // ignored
    }

    if (isForced) {
      stopSelf()

      try {
        pfd?.close()
      } catch (ignored: Exception) {
        ignored.printStackTrace()
        // ignored
      }
    }
  }

  private fun createNotificationChannel(): String {
    val channelId = "FUBUKI_CHANNEL_DEFAULT_ID"
    val channelName = "fubuki channel default"
    val chan = NotificationChannel(
      channelId,
      channelName,
      NotificationManager.IMPORTANCE_HIGH
    )
    getNotificationManager()?.createNotificationChannel(chan)
    return channelId
  }

  private fun getNotificationManager(): NotificationManager? {
    return this.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
  }

  companion object {
    private const val TAG = "FubukiVpnService"
    private const val ARG_FUBUKI_NODE_CONFIG = TAG + "_arg_fubuki_node_config"

    private const val FUBUKI_DEFAULT_DNS_SERVER = "1.1.1.1"
    private const val FUBUKI_VPN_SERVICE_NOTIFICATION_ID = 0x0900

    fun start(
      context: Context,
      fubukiNodeConfig: FubukiNodeConfig
    ) {
      val configJsonString = Json.encodeToString(fubukiNodeConfig)
      context.startForegroundService(
        Intent().apply {
          setPackage(context.packageName)
          putExtra(ARG_FUBUKI_NODE_CONFIG, configJsonString)
        }
      )
    }
  }
}