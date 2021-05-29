package hu.szacskesz.mobile.tasklist.core.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class Task(
    val id: Int = 0,
    val description: String,
    val done: Boolean,
    val deadline: Date?,
    val listId: Int?
) : Parcelable

@Parcelize
data class TaskWithTaskListName(
    val id: Int = 0,
    val description: String,
    val done: Boolean,
    val deadline: Date?,
    val listId: Int?,
    val listName: String?
) : Parcelable

@Parcelize
data class TaskWithTaskList(
    val id: Int = 0,
    val description: String,
    val done: Boolean,
    val deadline: Date?,
    val list: TaskList?
) : Parcelable
