package hu.szacskesz.mobile.tasklist.core.domain

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class TaskList(
    val id: Int = 0,
    val name: String,
): Parcelable

@Parcelize
data class TaskListWithTasksCount(
    val id: Int = 0,
    val name: String,
    val tasksCount: Int,
    val overdueTasksCount: Int,
): Parcelable
