package hu.szacskesz.mobile.tasklist.core.domain

import java.io.Serializable

data class Settings(
    val notificationsEnabled: Boolean,
    val soundNotificationsEnabled: Boolean,
    //TODO //val soundNotificationPath: String,
    val vibrationNotificationsEnabled: Boolean,
    val dailySummaryEnabled: Boolean,
    //TODO //val dailySummaryTime: Time
    val defaultGroup: Group,
): Serializable
