package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskRepository
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications


class DeleteTaskWithTaskNotifications(private val repository: TaskRepository) {
    suspend operator fun invoke(taskWithTaskNotifications: TaskWithTaskNotifications) = repository.delete(taskWithTaskNotifications)
}
