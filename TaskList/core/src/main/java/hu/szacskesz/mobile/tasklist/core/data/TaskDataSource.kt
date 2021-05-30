package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskListName
import java.util.*


interface TaskDataSource {
    suspend fun create(task: Task)
    suspend fun read(listId: Int?, isFinished: Boolean?) : List<Task>
    suspend fun readWithTaskList(listId: Int?, isFinished: Boolean?) : List<TaskWithTaskList>
    suspend fun readWithTaskListName(listId: Int?, isFinished: Boolean?) : List<TaskWithTaskListName>
    suspend fun getTaskCount(from: Date, to: Date, isOverdue: Boolean): Int
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
}
