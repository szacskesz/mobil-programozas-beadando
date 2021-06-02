package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotification
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotificationWithTask
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications
import java.util.*


class TaskRepository(
    private val dataSource: TaskDataSource,
    afterTaskNotificationCreated: ((TaskNotificationWithTask) -> Unit)?,
    afterTaskNotificationDeleted: ((TaskNotificationWithTask) -> Unit)?,
) {
    init {
        dataSource.setAfterTaskNotificationCreated(afterTaskNotificationCreated)
        dataSource.setAfterTaskNotificationDeleted(afterTaskNotificationDeleted)
    }

    suspend fun read(id: Int?, listId: Int?, isFinished: Boolean?) = dataSource.read(id, listId, isFinished)
    suspend fun create(task: Task) = dataSource.create(task)
    suspend fun update(task: Task) = dataSource.update(task)
    suspend fun delete(task: Task) = dataSource.delete(task)

    suspend fun readWithTaskListName(id: Int?, listId: Int?, isFinished: Boolean?) = dataSource .readWithTaskListName(id, listId, isFinished)
    suspend fun readWithTaskList(id: Int?, listId: Int?, isFinished: Boolean?) = dataSource.readWithTaskList(id, listId, isFinished)

    suspend fun getTaskCount(from: Date, to: Date, isOverdue: Boolean) = dataSource.getTaskCount(from, to, isOverdue)

    suspend fun readWithTaskNotifications(id: Int?, listId: Int?, isFinished: Boolean?) = dataSource.readWithTaskNotifications(id, listId, isFinished)
    suspend fun create(taskWithTaskNotifications: TaskWithTaskNotifications) = dataSource.create(taskWithTaskNotifications)
    suspend fun update(taskWithTaskNotifications: TaskWithTaskNotifications) = dataSource.update(taskWithTaskNotifications)
    suspend fun delete(taskWithTaskNotifications: TaskWithTaskNotifications) = dataSource.delete(taskWithTaskNotifications)

    suspend fun readByTaskNotificationId(id: Int) = dataSource.readByTaskNotificationId(id)
}
