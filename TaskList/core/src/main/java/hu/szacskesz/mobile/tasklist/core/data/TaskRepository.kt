package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.Task

class TaskRepository(private val dataSource: TaskDataSource) {
    suspend fun create(task: Task) = dataSource.create(task)
    suspend fun read() = dataSource.read()
    suspend fun readWithTaskList() = dataSource.readWithTaskList()
    suspend fun readWithTaskListName() = dataSource.readWithTaskListName()
    suspend fun update(task: Task) = dataSource.update(task)
    suspend fun delete(task: Task) = dataSource.delete(task)
}