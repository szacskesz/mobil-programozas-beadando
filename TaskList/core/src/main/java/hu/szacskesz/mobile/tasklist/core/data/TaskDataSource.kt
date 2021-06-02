package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.*
import java.util.*


interface TaskDataSource {
    fun setAfterTaskNotificationCreated(afterTaskNotificationCreated: ((TaskNotificationWithTask) -> Unit)?)
    fun setAfterTaskNotificationDeleted(afterTaskNotificationDeleted: ((TaskNotificationWithTask) -> Unit)?)

    suspend fun read(id: Int?, listId: Int?, isFinished: Boolean?): List<Task>
    suspend fun create(task: Task): Int
    suspend fun update(task: Task)
    suspend fun delete(task: Task)

    suspend fun readWithTaskListName(id: Int?, listId: Int?, isFinished: Boolean?): List<TaskWithTaskListName>
    suspend fun readWithTaskList(id: Int?, listId: Int?, isFinished: Boolean?): List<TaskWithTaskList>

    suspend fun getTaskCount(from: Date, to: Date, isOverdue: Boolean): Int

    suspend fun readWithTaskNotifications(id: Int?, listId: Int?, isFinished: Boolean?) : List<TaskWithTaskNotifications>
    suspend fun create(taskWithTaskNotifications: TaskWithTaskNotifications): Int
    suspend fun update(taskWithTaskNotifications: TaskWithTaskNotifications)
    suspend fun delete(taskWithTaskNotifications: TaskWithTaskNotifications)

    suspend fun readByTaskNotificationId(id: Int): Task?
}
