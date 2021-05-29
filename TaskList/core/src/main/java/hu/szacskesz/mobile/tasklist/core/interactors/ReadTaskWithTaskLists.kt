package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskRepository


class ReadTaskWithTaskLists(private val repository: TaskRepository) {
    suspend operator fun invoke(listId: Int?, isFinished: Boolean?) = repository.readWithTaskList(listId, isFinished)
}
