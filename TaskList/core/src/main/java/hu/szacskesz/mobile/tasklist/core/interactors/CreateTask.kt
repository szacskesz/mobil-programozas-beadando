package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskRepository
import hu.szacskesz.mobile.tasklist.core.domain.Task


class CreateTask(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.create(task)
}
