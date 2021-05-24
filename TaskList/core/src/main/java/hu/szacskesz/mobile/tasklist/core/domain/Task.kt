package hu.szacskesz.mobile.tasklist.core.domain

import java.util.Date

data class Task(
    val id: Int,
    val description: String,
    val list: TaskList,
    val done: Boolean,
    val repeating: Boolean,
    val deadline: Date, // TODO
)
