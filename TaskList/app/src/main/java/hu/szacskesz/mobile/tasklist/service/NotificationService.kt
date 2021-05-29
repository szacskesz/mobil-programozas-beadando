package hu.szacskesz.mobile.tasklist.service

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotification
import java.util.*


object NotificationService {

    fun setTaskNotification(context: Context, task: Task, taskNotification: TaskNotification) {
        val notification = createNotificationForTask(context, task)
    }

    fun setSummaryNotification(context: Context) {
        //TODO hogy legyen, legkérdezi a broadcast receiver a DAO-ból és megjeleníti?
    }

    private fun createNotificationForTask(context: Context, task: Task): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Title") //TODO:Feladat határidő: 20:33
            .setContentText("Message") //TODO task.description
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setChannelId(NOTIFICATION_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()
    }

    private fun createNotificationForSummary(context: Context): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Title") //TODO:Napi összefoglaló
            //TODO Feladatok ma: task::count & overdue task count
//            .setStyle(NotificationCompat.BigTextStyle().bigText("longText")) //TODO uses this for multiline noti
            .setContentText("Message2") //TODO uses this for small notification
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setChannelId(NOTIFICATION_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()
    }

    private fun scheduleNotification(
        context: Context,
        notification: Notification,
        notificationId: Int,
        dateTime: Date,
        isSummary: Boolean,
    ) {
        val notificationIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        notificationIntent.putExtra(INTENT_KEY_NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(INTENT_KEY_NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if(isSummary) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP , dateTime.time, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP , dateTime.time, pendingIntent)
        }
    }

    private fun cancelNotification(
        context: Context,
        notificationId: Int,
    ) {
        val notificationIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
