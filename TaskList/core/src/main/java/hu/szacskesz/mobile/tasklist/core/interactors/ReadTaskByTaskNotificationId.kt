package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskRepository


class ReadTaskByTaskNotificationId(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int) = repository.readByTaskNotificationId(id)
}
