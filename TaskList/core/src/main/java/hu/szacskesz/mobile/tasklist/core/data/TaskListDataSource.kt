package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskListWithTasksCount


interface TaskListDataSource {
    suspend fun read(id: Int?): List<TaskList>
    suspend fun create(taskList: TaskList): Int
    suspend fun update(taskList: TaskList)
    suspend fun delete(taskList: TaskList)

    suspend fun readWithTasksCount(id: Int?): List<TaskListWithTasksCount>
}
