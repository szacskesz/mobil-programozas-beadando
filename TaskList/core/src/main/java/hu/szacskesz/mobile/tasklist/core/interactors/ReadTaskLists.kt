package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskListRepository


class ReadTaskLists(private val repository: TaskListRepository) {
    suspend operator fun invoke() = repository.read();
}
