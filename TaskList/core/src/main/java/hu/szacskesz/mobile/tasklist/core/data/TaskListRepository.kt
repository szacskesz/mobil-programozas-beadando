package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.TaskList

class TaskListRepository(private val dataSource: TaskListDataSource) {
    suspend fun create(taskList: TaskList) = dataSource.create(taskList)
    suspend fun read() = dataSource.read()
    suspend fun update(taskList: TaskList) = dataSource.update(taskList)
    suspend fun delete(taskList: TaskList) = dataSource.delete(taskList)
}