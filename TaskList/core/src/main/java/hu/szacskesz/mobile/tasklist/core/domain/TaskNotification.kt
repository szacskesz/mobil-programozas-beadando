package hu.szacskesz.mobile.tasklist.core.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class TaskNotification(
    val id: Int,
    val datetime: Date,
    val taskId: Int,
) : Parcelable

@Parcelize
data class TaskNotificationWithTask(
    val id: Int,
    val datetime: Date,
    val taskId: Int,
    val task: Task,
) : Parcelable