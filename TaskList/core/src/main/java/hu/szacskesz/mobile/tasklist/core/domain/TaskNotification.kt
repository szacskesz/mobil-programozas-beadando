package hu.szacskesz.mobile.tasklist.core.domain

import java.util.Date


//TODO
data class TaskNotification(
    val id: Int,
    val task: Task,
    val diffToDeadline: Date, //TODO
)
