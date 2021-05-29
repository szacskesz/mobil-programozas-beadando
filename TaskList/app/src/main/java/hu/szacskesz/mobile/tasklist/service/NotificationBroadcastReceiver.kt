package hu.szacskesz.mobile.tasklist.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build


//TODO
val INTENT_KEY_NOTIFICATION_ID = "notification-id"
val INTENT_KEY_NOTIFICATION = "notification"
val NOTIFICATION_CHANNEL_ID = "1" //TODO: R.string.channel_id
val NOTIFICATION_CHANNEL_NAME = "Name"
val SUMMARY_NOTIFICATION_ID = -1

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = intent.getParcelableExtra<Notification>(INTENT_KEY_NOTIFICATION)
        val id = intent.getIntExtra(INTENT_KEY_NOTIFICATION_ID, 0)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }
}
