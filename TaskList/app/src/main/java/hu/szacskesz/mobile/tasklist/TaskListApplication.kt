package hu.szacskesz.mobile.tasklist

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.converters.toTaskNotification
import hu.szacskesz.mobile.tasklist.core.data.TaskListRepository
import hu.szacskesz.mobile.tasklist.core.data.TaskRepository
import hu.szacskesz.mobile.tasklist.core.interactors.*
import hu.szacskesz.mobile.tasklist.framework.Interactors
import hu.szacskesz.mobile.tasklist.framework.db.datasource.RoomTaskDataSource
import hu.szacskesz.mobile.tasklist.framework.db.datasource.RoomTaskListDataSource
import hu.szacskesz.mobile.tasklist.service.NotificationHelperService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TaskListApplication : Application() {

    lateinit var taskRepository: TaskRepository
    lateinit var taskListRepository: TaskListRepository

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        taskRepository = TaskRepository(
            RoomTaskDataSource(this), {
                GlobalScope.launch {
                    NotificationHelperService.scheduleNotificationForTask(
                        this@TaskListApplication,
                        it.task,
                        it.toTaskNotification()
                    )
                }
            }, {
                NotificationHelperService.unscheduleNotificationForTask(
                    this@TaskListApplication,
                    it.task,
                    it.toTaskNotification()
                )
            }
        )
        taskListRepository = TaskListRepository(RoomTaskListDataSource(this))

        CommonViewModelFactory.inject(
            this,
            Interactors(
                ReadTasks(taskRepository),
                CreateTask(taskRepository),
                UpdateTask(taskRepository),
                DeleteTask(taskRepository),

                ReadTaskWithTaskListNames(taskRepository),
                ReadTaskWithTaskLists(taskRepository),

                GetTaskCount(taskRepository),

                ReadTaskWithTaskNotifications(taskRepository),
                CreateTaskWithTaskNotifications(taskRepository),
                UpdateTaskWithTaskNotifications(taskRepository),
                DeleteTaskWithTaskNotifications(taskRepository),

                ReadTaskByTaskNotificationId(taskRepository),

                ReadTaskLists(taskListRepository),
                CreateTaskList(taskListRepository),
                UpdateTaskList(taskListRepository),
                DeleteTaskList(taskListRepository),

                ReadTaskListWithTasksCounts(taskListRepository),
            )
        )

        NotificationHelperService.createNotificationChannel(this)
        NotificationHelperService.handleSettingsChangeForNotifications(this)
    }
}
