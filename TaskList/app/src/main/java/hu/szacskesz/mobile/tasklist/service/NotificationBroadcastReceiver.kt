package hu.szacskesz.mobile.tasklist.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationJobIntentService.enqueueWork(
            context,
            Intent(context, NotificationJobIntentService::class.java).putExtras(intent)
        )
    }
}
