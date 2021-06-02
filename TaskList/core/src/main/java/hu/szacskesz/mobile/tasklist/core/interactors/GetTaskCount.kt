package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.TaskRepository
import java.util.*


class GetTaskCount(private val repository: TaskRepository) {
    suspend operator fun invoke(from: Date, to: Date, isOverdue: Boolean) = repository.getTaskCount(from, to, isOverdue)
}
