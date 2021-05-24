package hu.szacskesz.mobile.tasklist.framework

import hu.szacskesz.mobile.tasklist.core.interactors.*

data class Interactors(
    val createTaskList: CreateTaskList,
    val readTaskLists: ReadTaskLists,
    val updateTaskList: UpdateTaskList,
    val deleteTaskList: DeleteTaskList,
)
