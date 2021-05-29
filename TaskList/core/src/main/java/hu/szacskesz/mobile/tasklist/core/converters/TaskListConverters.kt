package hu.szacskesz.mobile.tasklist.core.converters

import hu.szacskesz.mobile.tasklist.core.domain.*


object TaskListConverters {
    fun fromTaskListWithTasksCountToTaskList(from: TaskListWithTasksCount) = TaskList(
        id = from.id,
        name = from.name
    )
}

fun TaskListWithTasksCount.toTaskList() = TaskListConverters.fromTaskListWithTasksCountToTaskList(this)
