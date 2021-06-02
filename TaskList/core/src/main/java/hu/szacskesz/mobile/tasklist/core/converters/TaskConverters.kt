package hu.szacskesz.mobile.tasklist.core.converters

import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskListName
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications


object TaskConverters {
    fun fromTaskWithTaskNotificationsToTask(from: TaskWithTaskNotifications) = Task(
        id = from.id,
        description = from.description,
        done = from.done,
        deadline = from.deadline,
        listId = from.listId,
    )

    fun fromTaskWithTaskListNameToTask(from: TaskWithTaskListName) = Task(
        id = from.id,
        description = from.description,
        done = from.done,
        deadline = from.deadline,
        listId = from.listId
    )

    fun fromTaskWithTaskListToTask(from: TaskWithTaskList) = Task(
        id = from.id,
        description = from.description,
        done = from.done,
        deadline = from.deadline,
        listId = from.list?.id
    )

    fun fromTaskWithTaskListToTaskWithTaskListName(from: TaskWithTaskList) = TaskWithTaskListName(
        id = from.id,
        description = from.description,
        done = from.done,
        deadline = from.deadline,
        listId = from.list?.id,
        listName = from.list?.name
    )
}

fun TaskWithTaskNotifications.toTask() = TaskConverters.fromTaskWithTaskNotificationsToTask(this)
fun TaskWithTaskListName.toTask() = TaskConverters.fromTaskWithTaskListNameToTask(this)
fun TaskWithTaskList.toTask() = TaskConverters.fromTaskWithTaskListToTask(this)
fun TaskWithTaskList.toTaskWithTaskListName() = TaskConverters.fromTaskWithTaskListToTaskWithTaskListName(this)
