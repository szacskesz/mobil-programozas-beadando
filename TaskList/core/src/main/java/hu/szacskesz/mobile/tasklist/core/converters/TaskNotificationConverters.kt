package hu.szacskesz.mobile.tasklist.core.converters

import hu.szacskesz.mobile.tasklist.core.domain.TaskNotification
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotificationWithTask


object TaskNotificationConverters {
    fun fromTaskNotificationWithTaskToTaskNotification(from: TaskNotificationWithTask) = TaskNotification(
        id = from.id,
        datetime = from.datetime,
        taskId = from.taskId,
    )
}

fun TaskNotificationWithTask.toTaskNotification() = TaskNotificationConverters.fromTaskNotificationWithTaskToTaskNotification(this)
