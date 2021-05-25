package hu.szacskesz.mobile.tasklist

import android.app.Application
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.data.TaskListRepository
import hu.szacskesz.mobile.tasklist.core.interactors.*
import hu.szacskesz.mobile.tasklist.framework.Interactors
import hu.szacskesz.mobile.tasklist.framework.db.datasource.RoomTaskListDataSource

class TaskListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val taskListRepository = TaskListRepository(RoomTaskListDataSource(this))

        CommonViewModelFactory.inject(
            this,
            Interactors(
                CreateTaskList(taskListRepository),
                ReadTaskLists(taskListRepository),
                UpdateTaskList(taskListRepository),
                DeleteTaskList(taskListRepository),
            )
        )
    }
}