package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskRepository


class ReadTaskWithTaskNotifications(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int?, listId: Int?, isFinished: Boolean?) = repository.readWithTaskNotifications(id, listId, isFinished)
}
