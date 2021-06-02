package hu.szacskesz.mobile.tasklist.framework.db.converters

import hu.szacskesz.mobile.tasklist.core.domain.TaskNotification
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotificationWithTask
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskNotificationEntity
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskNotificationWithTaskEntity


object TaskNotificationConverter {
    fun dtoToEntity(dto: TaskNotification) = TaskNotificationEntity(
        id = dto.id,
        datetime = dto.datetime,
        taskId = dto.taskId,
    )

    fun entityToDto(entity: TaskNotificationEntity) = TaskNotification(
        id = entity.id,
        datetime = entity.datetime,
        taskId = entity.taskId,
    )
}

fun TaskNotificationEntity.toDto() = TaskNotificationConverter.entityToDto(this)
fun TaskNotification.toEntity() = TaskNotificationConverter.dtoToEntity(this)

object TaskNotificationWithTaskConverter {
    fun dtoToEntity(dto: TaskNotificationWithTask) = TaskNotificationWithTaskEntity(
        notification = TaskNotificationEntity(
            id = dto.id,
            datetime = dto.datetime,
            taskId = dto.taskId,
        ),
        task = dto.task.toEntity()
    )

    fun entityToDto(entity: TaskNotificationWithTaskEntity) = TaskNotificationWithTask(
        id = entity.notification.id,
        datetime = entity.notification.datetime,
        taskId  = entity.notification.taskId,
        task = entity.task.toDto()
    )
}

fun TaskNotificationWithTaskEntity.toDto() = TaskNotificationWithTaskConverter.entityToDto(this)
fun TaskNotificationWithTask.toEntity() = TaskNotificationWithTaskConverter.dtoToEntity(this)
