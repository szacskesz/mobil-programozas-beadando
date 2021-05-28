package hu.szacskesz.mobile.tasklist.framework.db.datasource

import android.content.Context
import hu.szacskesz.mobile.tasklist.core.data.TaskDataSource
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskListName
import hu.szacskesz.mobile.tasklist.framework.db.AppDatabase
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskEntity

class RoomTaskDataSource(val context: Context) : TaskDataSource {

    private val taskDao = AppDatabase.getInstance(context).taskDao()

    override suspend fun create(task: Task) {
        return taskDao.create(
            TaskEntity(
                task.id,
                task.description,
                task.done,
                task.deadline,
                task.listId
            )
        )
    }

    override suspend fun read(): List<Task> {
        return taskDao.read().map {
            Task(
                it.id,
                it.description,
                it.done,
                it.deadline,
                it.listId
            )
        }
    }

    override suspend fun readWithTaskList(): List<TaskWithTaskList> {
        return taskDao.readWithTaskListEntity().map {
            TaskWithTaskList(
                it.task.id,
                it.task.description,
                it.task.done,
                it.task.deadline,
                if(it.list == null) null else TaskList(
                    it.list.id,
                    it.list.name
                )
            )
        }
    }

    override suspend fun readWithTaskListName(): List<TaskWithTaskListName> {
        return taskDao.readWithTaskListEntity().map {
            TaskWithTaskListName(
                it.task.id,
                it.task.description,
                it.task.done,
                it.task.deadline,
                it.list?.id,
                it.list?.name
            )
        }
    }

    override suspend fun update(task: Task)  {
        return taskDao.update(
            TaskEntity(
                task.id,
                task.description,
                task.done,
                task.deadline,
                task.listId
            )
        )
    }

    override suspend fun delete(task: Task) {
        return taskDao.delete(
            TaskEntity(
                task.id,
                task.description,
                task.done,
                task.deadline,
                task.listId
            )
        )
    }
}