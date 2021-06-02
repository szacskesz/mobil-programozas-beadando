package hu.szacskesz.mobile.tasklist.framework.db.converters

import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskEntity
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskWithTaskListEntity
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskWithTaskNotificationsEntity


object TaskConverter {
    fun dtoToEntity(dto: Task) = TaskEntity(
        id = dto.id,
        description = dto.description,
        done = dto.done,
        deadline = dto.deadline,
        listId = dto.listId,
    )

    fun entityToDto(entity: TaskEntity) = Task(
        id = entity.id,
        description = entity.description,
        done = entity.done,
        deadline = entity.deadline,
        listId = entity.listId,
    )
}

fun TaskEntity.toDto() = TaskConverter.entityToDto(this)
fun Task.toEntity() = TaskConverter.dtoToEntity(this)

object TaskWithTaskListConverter {
    fun dtoToEntity(dto: TaskWithTaskList) = TaskWithTaskListEntity(
        task = TaskEntity(
            id = dto.id,
            description = dto.description,
            done = dto.done,
            deadline = dto.deadline,
            listId = dto.list?.id
        ),
        list = dto.list?.toEntity()
    )

    fun entityToDto(entity: TaskWithTaskListEntity) = TaskWithTaskList(
        id = entity.task.id,
        description = entity.task.description,
        done = entity.task.done,
        deadline = entity.task.deadline,
        list = entity.list?.toDto()
    )
}

fun TaskWithTaskListEntity.toDto() = TaskWithTaskListConverter.entityToDto(this)
fun TaskWithTaskList.toEntity() = TaskWithTaskListConverter.dtoToEntity(this)

object TaskWithTaskNotificationsConverter {
    fun dtoToEntity(dto: TaskWithTaskNotifications) = TaskWithTaskNotificationsEntity(
        task = TaskEntity(
            id = dto.id,
            description = dto.description,
            done = dto.done,
            deadline = dto.deadline,
            listId = dto.listId,
        ),
        notifications = dto.notifications.map { it.toEntity() }
    )

    fun entityToDto(entity: TaskWithTaskNotificationsEntity) = TaskWithTaskNotifications(
        id = entity.task.id,
        description = entity.task.description,
        done = entity.task.done,
        deadline = entity.task.deadline,
        listId = entity.task.listId,
        notifications = entity.notifications.map { it.toDto() }
    )
}

fun TaskWithTaskNotificationsEntity.toDto() = TaskWithTaskNotificationsConverter.entityToDto(this)
fun TaskWithTaskNotifications.toEntity() = TaskWithTaskNotificationsConverter.dtoToEntity(this)

