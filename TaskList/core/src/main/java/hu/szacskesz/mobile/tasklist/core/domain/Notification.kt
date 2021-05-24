package hu.szacskesz.mobile.tasklist.core.domain

import java.util.Date

data class Notification(
    val id: Int,
    val item: Item,
    val diffToDeadline: Date, //TODO
)
