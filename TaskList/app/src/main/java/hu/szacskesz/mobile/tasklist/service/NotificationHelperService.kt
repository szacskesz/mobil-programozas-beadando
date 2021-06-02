package hu.szacskesz.mobile.tasklist.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotification
import hu.szacskesz.mobile.tasklist.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

object NotificationHelperService {

    public fun createNotificationForTask(context: Context, task: Task): Notification {
        val title =
        if(task.deadline != null) context.getString(
            R.string.notification_title_task_with_deadline,
            StringBuilder()
                .append(DateFormat.getDateFormat(context).format(task.deadline!!))
                .append(" ")
                .append(DateFormat.getTimeFormat(context).format(task.deadline!!))
                .toString()
        ) else context.getString(R.string.notification_title_task_without_deadline)
        val content = task.description

        return createNotification(context, title, content)
    }

    public fun createNotificationForSummary(context: Context, taskCount: Int, overdueTaskCount: Int): Notification {
        val title = context.getString(R.string.notification_title_day_summary)
        val content = context.getString(R.string.notification_content_day_summary, taskCount, overdueTaskCount)

        return createNotification(context, title, content)
    }

    private fun createNotification(context: Context, title: String, content: String): Notification {
        return NotificationCompat.Builder(context, Constants.Notification.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setContentText(content)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setChannelId(Constants.Notification.CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .setLights(Color.WHITE, 3000, 3000)
            .build()
    }

    public fun scheduleNotificationForTask(context: Context, task: Task, taskNotification: TaskNotification) {
        scheduleNotification(
            context,
            taskNotification.id,
            taskNotification.datetime
        )
    }

    public fun unscheduleNotificationForTask(context: Context, task: Task, taskNotification: TaskNotification) {
        cancelNotification(context, taskNotification.id)
    }

    public fun scheduleNotificationForSummary(context: Context, dateTime: Date?) {
        if(dateTime != null ) {
            scheduleNotification(
                context,
                Constants.IntentExtra.Value.NOTIFICATION_ID_SUMMARY,
                dateTime
            )
        } else {
            cancelNotification(context, Constants.IntentExtra.Value.NOTIFICATION_ID_SUMMARY)
        }
    }

    private fun scheduleNotification(
        context: Context,
        notificationId: Int,
        dateTime: Date,
    ) {
        val isSummary = (notificationId == Constants.IntentExtra.Value.NOTIFICATION_ID_SUMMARY)

        val notificationIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        notificationIntent.putExtra(Constants.IntentExtra.Key.NOTIFICATION_ID, notificationId)

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

    public fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.Notification.CHANNEL_ID,
                Constants.Notification.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(1000, 1000, 1000)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.WHITE
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    lateinit var sharedPrefChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    @SuppressLint("SimpleDateFormat")
    public fun handleSettingsChangeForNotifications(context: Context) {

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPrefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, _ ->
            scheduleNotificationForSummary(context, null)

            val isNotificationsEnabled = prefs.getBoolean(context.getString(R.string.settings_notifications_key), false)
            val isDaySummaryEnabled = prefs.getBoolean(context.getString(R.string.settings_day_summary_key), false)
            val daySummaryTime: Calendar? = prefs.getString(context.getString(R.string.settings_day_summary_time_key), null)?.let {
                val date = SimpleDateFormat("HH:mm").parse(it)!!
                val cal = Calendar.getInstance()
                cal.time = date

                return@let cal
            }

            if(isNotificationsEnabled && isDaySummaryEnabled && daySummaryTime != null) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, daySummaryTime.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, daySummaryTime.get(Calendar.MINUTE))
                //for past times
                if(calendar.before(Calendar.getInstance())) calendar.add(Calendar.DAY_OF_MONTH, 1)

                scheduleNotificationForSummary(context, calendar.time)
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)
        sharedPrefChangeListener.onSharedPreferenceChanged(sharedPrefs, context.getString(R.string.settings_day_summary_time_key))
    }
}
