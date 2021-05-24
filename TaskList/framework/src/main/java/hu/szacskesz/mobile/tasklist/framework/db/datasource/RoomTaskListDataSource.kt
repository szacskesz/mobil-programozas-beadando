package hu.szacskesz.mobile.tasklist.framework.db.datasource

import android.content.Context
import hu.szacskesz.mobile.tasklist.core.data.TaskListDataSource
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.framework.db.AppDatabase
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskListEntity

class RoomTaskListDataSource(val context: Context) : TaskListDataSource {

    private val taskListDao = AppDatabase.getInstance(context).groupDao()

    override suspend fun create(taskList: TaskList) {
        return taskListDao.create( TaskListEntity(taskList.id, taskList.name) )
    }

    override suspend fun read(): List<TaskList> {
        return taskListDao.read().map {
            TaskList(it.id, it.name)
        }
    }

    override suspend fun update(taskList: TaskList)  {
        return taskListDao.create( TaskListEntity(taskList.id, taskList.name) )
    }

    override suspend fun delete(taskList: TaskList) {
        return taskListDao.delete( TaskListEntity(taskList.id, taskList.name) )
    }
}