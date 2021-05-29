package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskRepository


class ReadTaskWithTaskListNames(private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.readWithTaskListName();
}
