package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskListWithTasksCount


interface TaskListDataSource {
    suspend fun create(taskList: TaskList)
    suspend fun read() : List<TaskList>
    suspend fun readWithTasksCount() : List<TaskListWithTasksCount>
    suspend fun update(taskList: TaskList)
    suspend fun delete(taskList: TaskList)
}
