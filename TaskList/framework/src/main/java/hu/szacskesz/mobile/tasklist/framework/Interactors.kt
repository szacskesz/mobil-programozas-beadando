package hu.szacskesz.mobile.tasklist.framework

import hu.szacskesz.mobile.tasklist.core.interactors.*

data class Interactors(
    val createTask: CreateTask,
    val readTasks: ReadTasks,
    val readTaskWithTaskLists: ReadTaskWithTaskLists,
    val readTaskWithTaskListNames: ReadTaskWithTaskListNames,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask,

    val createTaskList: CreateTaskList,
    val readTaskLists: ReadTaskLists,
    val updateTaskList: UpdateTaskList,
    val deleteTaskList: DeleteTaskList,
)
