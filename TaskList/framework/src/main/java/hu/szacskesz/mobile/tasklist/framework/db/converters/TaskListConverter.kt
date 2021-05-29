package hu.szacskesz.mobile.tasklist.framework.db.converters

import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskListWithTasksCount
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskListEntity
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskListWithTasksCountEntity


object TaskListConverter {
    fun dtoToEntity(dto: TaskList) = TaskListEntity(
        id = dto.id,
        name = dto.name,
    )

    fun entityToDto(entity: TaskListEntity) = TaskList(
        id = entity.id,
        name = entity.name,
    )
}

fun TaskListEntity.toDto() = TaskListConverter.entityToDto(this)
fun TaskList.toEntity() = TaskListConverter.dtoToEntity(this)

object TaskListWithTasksCountEntityConverter {
    fun dtoToEntity(dto: TaskListWithTasksCount) = TaskListWithTasksCountEntity(
        taskList = TaskListEntity(
            id = dto.id,
            name = dto.name,
        ),
        tasksCount = dto.tasksCount,
        overdueTasksCount = dto.overdueTasksCount,
    )

    fun entityToDto(entity: TaskListWithTasksCountEntity) = TaskListWithTasksCount(
        id = entity.taskList.id,
        name = entity.taskList.name,
        tasksCount = entity.tasksCount,
        overdueTasksCount = entity.overdueTasksCount,
    )
}

fun TaskListWithTasksCountEntity.toDto() = TaskListWithTasksCountEntityConverter.entityToDto(this)
fun TaskListWithTasksCount.toEntity() = TaskListWithTasksCountEntityConverter.dtoToEntity(this)
