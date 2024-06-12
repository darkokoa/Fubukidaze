package dev.darkokoa.fubukidaze

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dev.darkokoa.fubukidaze.android.androidModule
import dev.darkokoa.fubukidaze.android.service.FubukiVpnService
import dev.darkokoa.fubukidaze.core.koin.KoinInitializer
import dev.darkokoa.fubukidaze.core.log.FubukilLogger
import dev.darkokoa.fubukidaze.data.model.pojo.FubukiNodeConfig
import dev.darkokoa.fubukidaze.ui.screen.uncaughtexceptionlog.UncaughtExceptionLog
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext

class AndroidApp : Application() {
  companion object {
    lateinit var INSTANCE: AndroidApp
  }

  override fun onCreate() {
    INSTANCE = this
    registerUncaughtExceptionLog()
    super.onCreate()
    KoinInitializer.initialize(
      platformModules = listOf(androidModule),
      additionalBlock = { androidContext(this@AndroidApp) }
    )

    configureRuntimeLogger()
  }

  private fun configureRuntimeLogger() {
    getKoin().get<FubukilLogger>().init()
  }
}

class AppActivity : ComponentActivity() {

  companion object {
    lateinit var INSTANCE: AppActivity
  }

  private val requestPostNotificationPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

  private val requestVpnPermission =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode != RESULT_OK) {
        tryToRequestVpnPermission()
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    INSTANCE = this
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent { App() }

    tryToRequestVpnPermission()
  }

  @Suppress("MemberVisibilityCanBePrivate")
  fun tryToRequestVpnPermission() {
    val prepareIntent = VpnService.prepare(this)
    if (prepareIntent != null) {
      requestVpnPermission.launch(prepareIntent)
    }
  }

  @Suppress("MemberVisibilityCanBePrivate")
  fun tryToRequestPostNotificationPermission() {
    if ((ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.POST_NOTIFICATIONS
      ) != PackageManager.PERMISSION_GRANTED)
      && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    ) {
      requestPostNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
  }
}

internal actual fun openUrl(url: String?) {
  val uri = url?.let { Uri.parse(it) } ?: return
  val intent = Intent().apply {
    action = Intent.ACTION_VIEW
    data = uri
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  }
  AndroidApp.INSTANCE.startActivity(intent)
}

internal actual fun launchFubuki(config: FubukiNodeConfig) {
  val appContext = AndroidApp.INSTANCE
  val appActivity = AppActivity.INSTANCE

  FubukiVpnService.launch(appContext, config)

  appActivity.tryToRequestPostNotificationPermission()
}

internal actual fun terminateFubuki() {
  val context = AndroidApp.INSTANCE

  FubukiVpnService.terminate(context)
}

internal actual fun switchFubukiConnection(config: FubukiNodeConfig) {
  val context = AndroidApp.INSTANCE

  FubukiVpnService.switchConnection(context, config)
}

internal fun registerUncaughtExceptionLog() {
  Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
    GlobalNavigator.tryTransaction {
      push(UncaughtExceptionLog(throwable.stackTraceToString()))
    }
    throwable.printStackTrace()
  }
}
