package dev.darkokoa.fubukidaze

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import dev.darkokoa.fubukidaze.android.service.FubukiVpnService
import dev.darkokoa.fubukidaze.core.koin.KoinInitializer
import dev.darkokoa.fubukidaze.data.pojo.FubukiNodeConfig

class AndroidApp : Application() {
  companion object {
    lateinit var INSTANCE: AndroidApp
  }

  override fun onCreate() {
    super.onCreate()
    KoinInitializer.initialize()
    INSTANCE = this
  }
}

class AppActivity : ComponentActivity() {

  private val requestVpnPermission = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode != RESULT_OK) {
      tryToRequestVpnPermission()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent { App() }

    tryToRequestVpnPermission()
  }

  fun tryToRequestVpnPermission() {
    val prepareIntent = VpnService.prepare(this)
    if (prepareIntent != null) {
      requestVpnPermission.launch(prepareIntent)
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
  val context = AndroidApp.INSTANCE


  FubukiVpnService.start(context, config)
}