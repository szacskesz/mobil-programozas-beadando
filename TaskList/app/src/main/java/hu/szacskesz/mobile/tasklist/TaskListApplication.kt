package hu.szacskesz.mobile.tasklist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.data.TaskListRepository
import hu.szacskesz.mobile.tasklist.core.data.TaskRepository
import hu.szacskesz.mobile.tasklist.core.interactors.*
import hu.szacskesz.mobile.tasklist.framework.Interactors
import hu.szacskesz.mobile.tasklist.framework.db.datasource.RoomTaskDataSource
import hu.szacskesz.mobile.tasklist.framework.db.datasource.RoomTaskListDataSource
import hu.szacskesz.mobile.tasklist.service.NOTIFICATION_CHANNEL_ID
import hu.szacskesz.mobile.tasklist.service.NOTIFICATION_CHANNEL_NAME


class TaskListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val taskRepository = TaskRepository(RoomTaskDataSource(this))
        val taskListRepository = TaskListRepository(RoomTaskListDataSource(this))

        CommonViewModelFactory.inject(
            this,
            Interactors(
                CreateTask(taskRepository),
                ReadTasks(taskRepository),
                ReadTaskWithTaskLists(taskRepository),
                ReadTaskWithTaskListNames(taskRepository),
                UpdateTask(taskRepository),
                DeleteTask(taskRepository),
                CreateTaskList(taskListRepository),
                ReadTaskLists(taskListRepository),
                ReadTaskListWithTasksCounts(taskListRepository),
                UpdateTaskList(taskListRepository),
                DeleteTaskList(taskListRepository),
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
