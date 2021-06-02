package hu.szacskesz.mobile.tasklist.framework

import hu.szacskesz.mobile.tasklist.core.interactors.*

data class Interactors(
    val readTasks: ReadTasks,
    val createTask: CreateTask,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask,

    val readTaskWithTaskListNames: ReadTaskWithTaskListNames,
    val readTaskWithTaskLists: ReadTaskWithTaskLists,

    val getTaskCount: GetTaskCount,

    val readTaskWithTaskNotifications: ReadTaskWithTaskNotifications,
    val createTaskWithTaskNotifications: CreateTaskWithTaskNotifications,
    val updateTaskWithTaskNotifications: UpdateTaskWithTaskNotifications,
    val deleteTaskWithTaskNotifications: DeleteTaskWithTaskNotifications,

    val readTaskByTaskNotificationId: ReadTaskByTaskNotificationId,

    val readTaskLists: ReadTaskLists,
    val createTaskList: CreateTaskList,
    val updateTaskList: UpdateTaskList,
    val deleteTaskList: DeleteTaskList,

    val readTaskListWithTasksCounts: ReadTaskListWithTasksCounts,
)
