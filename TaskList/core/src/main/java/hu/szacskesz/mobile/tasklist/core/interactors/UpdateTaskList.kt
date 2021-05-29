package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskListRepository
import hu.szacskesz.mobile.tasklist.core.domain.TaskList


class UpdateTaskList(private val repository: TaskListRepository) {
    suspend operator fun invoke(taskList: TaskList) = repository.update(taskList)
}
