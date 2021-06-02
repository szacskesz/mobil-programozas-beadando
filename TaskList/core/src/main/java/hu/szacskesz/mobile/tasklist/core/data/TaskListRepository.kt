package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.TaskList


class TaskListRepository(private val dataSource: TaskListDataSource) {
    suspend fun read(id: Int?) = dataSource.read(id)
    suspend fun create(taskList: TaskList) = dataSource.create(taskList)
    suspend fun update(taskList: TaskList) = dataSource.update(taskList)
    suspend fun delete(taskList: TaskList) = dataSource.delete(taskList)

    suspend fun readWithTasksCount(id: Int?) = dataSource.readWithTasksCount(id)
}
