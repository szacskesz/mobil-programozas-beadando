package hu.szacskesz.mobile.tasklist.core.domain

import java.util.Date

data class Item(
    val id: Int,
    val description: String,
    val group: Group,
    val done: Boolean,
    val repeating: Boolean,
    val deadline: Date, // TODO
)
