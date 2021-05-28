package hu.szacskesz.mobile.tasklist.core.domain

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class TaskList(
    val id: Int = 0,
    val name: String,
): Parcelable
