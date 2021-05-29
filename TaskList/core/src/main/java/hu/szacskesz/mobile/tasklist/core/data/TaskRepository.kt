package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.Task


class TaskRepository(private val dataSource: TaskDataSource) {
    suspend fun create(task: Task) = dataSource.create(task)
    suspend fun read(listId: Int?, isFinished: Boolean?) = dataSource.read(listId, isFinished)
    suspend fun readWithTaskList(listId: Int?, isFinished: Boolean?) = dataSource.readWithTaskList(listId, isFinished)
    suspend fun readWithTaskListName(listId: Int?, isFinished: Boolean?) = dataSource.readWithTaskListName(listId, isFinished)
    suspend fun update(task: Task) = dataSource.update(task)
    suspend fun delete(task: Task) = dataSource.delete(task)
}
