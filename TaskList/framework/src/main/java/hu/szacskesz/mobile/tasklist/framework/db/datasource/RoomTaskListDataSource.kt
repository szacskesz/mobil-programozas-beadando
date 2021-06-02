package hu.szacskesz.mobile.tasklist.framework.db.datasource

import android.content.Context
import hu.szacskesz.mobile.tasklist.core.data.TaskListDataSource
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskListWithTasksCount
import hu.szacskesz.mobile.tasklist.framework.db.AppDatabase
import hu.szacskesz.mobile.tasklist.framework.db.converters.toDto
import hu.szacskesz.mobile.tasklist.framework.db.converters.toEntity
import java.util.*


class RoomTaskListDataSource(val context: Context) : TaskListDataSource {

    private val taskListDao = AppDatabase.getInstance(context).taskListDao()

    override suspend fun read(id: Int?): List<TaskList> {
        return taskListDao.read(id).map { it.toDto() }
    }

    override suspend fun create(taskList: TaskList): Int {
        return taskListDao.create( taskList.toEntity() ).toInt()
    }

    override suspend fun update(taskList: TaskList)  {
        return taskListDao.update( taskList.toEntity() )
    }

    override suspend fun delete(taskList: TaskList) {
        return taskListDao.delete( taskList.toEntity() )
    }



    override suspend fun readWithTasksCount(id: Int?): List<TaskListWithTasksCount> {
        return taskListDao.readWithTasksCount(id, Date()).map { it.toDto() }
    }
}
