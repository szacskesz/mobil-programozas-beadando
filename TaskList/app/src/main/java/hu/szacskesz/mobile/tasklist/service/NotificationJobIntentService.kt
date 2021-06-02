package hu.szacskesz.mobile.tasklist.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.TaskListApplication
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareJonIntentService
import hu.szacskesz.mobile.tasklist.utils.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class NotificationJobIntentService: BaseLanguageAwareJonIntentService() {
    @SuppressLint("SimpleDateFormat")
    override fun onHandleWork(intent: Intent) {
        val context = this

        GlobalScope.launch {
            val notificationId = intent.getIntExtra(Constants.IntentExtra.Key.NOTIFICATION_ID, 0)
            val isSummary = (notificationId == Constants.IntentExtra.Value.NOTIFICATION_ID_SUMMARY)

            Log.d("NOTIFICATION_SERVICE", "Handling notification for id: $notificationId")

            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
            val isNotificationsEnabled = sharedPrefs.getBoolean(context.getString(R.string.settings_notifications_key), false)
            val isDaySummaryEnabled = sharedPrefs.getBoolean(context.getString(R.string.settings_day_summary_key), false)
            val daySummaryTime: Calendar? = sharedPrefs.getString(context.getString(R.string.settings_day_summary_time_key), null)?.let {
                val date = SimpleDateFormat("HH:mm").parse(it)!!
                val cal = Calendar.getInstance()
                cal.time = date

                return@let cal
            }

            val taskRepository = (application as TaskListApplication).taskRepository

            if(isNotificationsEnabled) {
                if(isSummary) {
                    if(isDaySummaryEnabled && daySummaryTime != null) {
                        val from = Calendar.getInstance()
                        from.set(Calendar.HOUR_OF_DAY, 0)
                        from.set(Calendar.MINUTE, 0)
                        from.set(Calendar.SECOND, 0)
                        from.set(Calendar.MILLISECOND, 0)

                        val to = Calendar.getInstance()
                        to.set(Calendar.HOUR_OF_DAY, 0)
                        to.set(Calendar.MINUTE, 0)
                        to.set(Calendar.SECOND, 0)
                        to.set(Calendar.MILLISECOND, 0)
                        to.add(Calendar.DAY_OF_MONTH, 1)

                        val taskCount = taskRepository.getTaskCount(from.time, to.time,false)
                        val overdueTaskCount = taskRepository.getTaskCount(from.time, to.time,true)

                        (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                            notificationId,
                            NotificationHelperService.createNotificationForSummary(
                                context,
                                taskCount,
                                overdueTaskCount
                            )
                        )
                    }
                } else {
                    val task = taskRepository.readByTaskNotificationId(notificationId)
                    if(task != null) {
                        Log.d("NOTIFICATION_SERVICE", "Showing notification for task: $task")
                        (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                            notificationId,
                            NotificationHelperService.createNotificationForTask(context, task)
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val JOB_ID = 1

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NotificationJobIntentService::class.java, JOB_ID, intent)
        }
    }
}

