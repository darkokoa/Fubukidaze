package dev.darkokoa.fubukidaze.android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import dev.darkokoa.fubukidaze.android.notification.FubukidazeNotification
import dev.darkokoa.fubukidaze.android.service.FubukiVpnService.Companion.Action.*
import dev.darkokoa.fubukidaze.android.util.fubuki.FubukiJNA
import dev.darkokoa.fubukidaze.core.base.util.AppCoroutineScope
import dev.darkokoa.fubukidaze.core.base.util.netmaskToPrefixLength
import dev.darkokoa.fubukidaze.data.model.pojo.FubukiNodeConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FubukiVpnService : VpnService(), KoinComponent {

  private val appCoroutineScope: AppCoroutineScope by inject()

  private val dazeNotification:FubukidazeNotification by inject()

  private val fubuki: FubukiJNA = FubukiJNA.INSTANCE

  private var isRunning = false
  private var pfd: ParcelFileDescriptor? = null
  private var fubukiHandle: FubukiJNA.FubukiHandle? = null


  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    @Suppress("DEPRECATION")
    when (intent?.getSerializableExtra(ARG_SERVICE_ACTION) as Action?) {
      Launch -> {
        setupAndLaunchFubuki(intent)

        dazeNotification.notifyFubukiVpnConnect(this, FUBUKI_VPN_SERVICE_NOTIFICATION_ID)
      }

      Terminate -> {
        terminateFubuki()
      }

      Switch -> {
        println("$TAG ARG_SERVICE_ACTION: Switch")
        terminateFubuki(false)
        setupAndLaunchFubuki(intent)
      }

      null -> {}
    }

    return START_STICKY
  }

  override fun onRevoke() {
    terminateFubuki()
  }

  private fun setupAndLaunchFubuki(intent: Intent?) {
    println("$TAG setupAndLaunchFubuki(intent: $intent)")

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

    pfd?.close()
    pfd = null

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
      terminateFubuki()
    }
  }

  private fun terminateFubuki(executeStopSelf: Boolean = true) {
    isRunning = false

    fubukiHandle?.let(fubuki::fubuki_stop)
    fubukiHandle = null

    pfd?.close()
    pfd = null

    if (executeStopSelf) {
      stopSelf()
    }
  }

  companion object {
    private const val TAG = "FubukiVpnService"
    private const val ARG_FUBUKI_NODE_CONFIG = TAG + "_arg_fubuki_node_config"
    private const val ARG_SERVICE_ACTION = TAG + "_arg_service_action"

    private const val FUBUKI_DEFAULT_DNS_SERVER = "1.1.1.1"
    const val FUBUKI_VPN_SERVICE_NOTIFICATION_ID = 0x0900

    enum class Action {
      Launch, Terminate, Switch
    }

    fun launch(
      context: Context,
      fubukiNodeConfig: FubukiNodeConfig
    ) {
      val configJsonString = Json.encodeToString(fubukiNodeConfig)
      context.startForegroundService(
        Intent(context, FubukiVpnService::class.java).apply {
          setPackage(context.packageName)
          putExtra(ARG_SERVICE_ACTION, Launch)
          putExtra(ARG_FUBUKI_NODE_CONFIG, configJsonString)
        }
      )
    }

    fun terminate(
      context: Context,
    ) {
      context.startService(Intent(context, FubukiVpnService::class.java).apply {
        setPackage(context.packageName)
        putExtra(ARG_SERVICE_ACTION, Terminate)
      })
    }

    fun switchConnection(
      context: Context,
      fubukiNodeConfig: FubukiNodeConfig
    ) {
      val configJsonString = Json.encodeToString(fubukiNodeConfig)

      context.startService(Intent(context, FubukiVpnService::class.java).apply {
        setPackage(context.packageName)
        putExtra(ARG_SERVICE_ACTION, Switch)
        putExtra(ARG_FUBUKI_NODE_CONFIG, configJsonString)
      })
    }
  }
}