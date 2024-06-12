package dev.darkokoa.fubukidaze.android.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.app.ServiceCompat
import dev.darkokoa.fubukidaze.AppActivity
import dev.darkokoa.fubukidaze.android.service.FubukiVpnService

class FubukidazeNotification(
  private val context: Context
) {

  companion object {
    private const val FUBUKIDAZE_NOTIFICATION_CHANNEL_ID = "FubukidazeNotificationChannel"
  }

  private val notificationManager: NotificationManager get() = context.getSystemService(NotificationManager::class.java)

  private fun buildReturnToAppPendingIntent(notificationId: Int) =
    PendingIntentCompat
      .getActivity(
        context,
        notificationId,
        Intent(context, AppActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT,
        false
      )

  fun notifyFubukiVpnConnect(
    service: FubukiVpnService,
    notificationId: Int
  ) {
    val notificationBuilder =
      NotificationCompat.Builder(
        context,
        FUBUKIDAZE_NOTIFICATION_CHANNEL_ID
      ).apply {
        setSmallIcon(android.R.drawable.ic_menu_compass)
        setContentTitle("Fubukidaze")
        setContentText("Fubuki vpn service active")
        setContentIntent(buildReturnToAppPendingIntent(notificationId))
      }

    notificationManager.createNotificationChannel(
      NotificationChannel(
        FUBUKIDAZE_NOTIFICATION_CHANNEL_ID,
        FUBUKIDAZE_NOTIFICATION_CHANNEL_ID,
        NotificationManager.IMPORTANCE_HIGH
      )
    )

    @Suppress("LocalVariableName") val FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED = 1024

    ServiceCompat.startForeground(
      service,
      notificationId,
      notificationBuilder.build(),
      FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
    )
  }

}