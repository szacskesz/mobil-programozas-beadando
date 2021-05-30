package hu.szacskesz.mobile.tasklist

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.data.TaskListRepository
import hu.szacskesz.mobile.tasklist.core.data.TaskRepository
import hu.szacskesz.mobile.tasklist.core.interactors.*
import hu.szacskesz.mobile.tasklist.framework.Interactors
import hu.szacskesz.mobile.tasklist.framework.db.datasource.RoomTaskDataSource
import hu.szacskesz.mobile.tasklist.framework.db.datasource.RoomTaskListDataSource


class TaskListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
    }
}
