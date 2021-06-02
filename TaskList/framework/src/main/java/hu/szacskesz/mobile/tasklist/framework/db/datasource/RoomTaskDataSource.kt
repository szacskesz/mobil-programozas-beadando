package hu.szacskesz.mobile.tasklist.framework.db.datasource

import android.content.Context
import hu.szacskesz.mobile.tasklist.core.converters.toTaskWithTaskListName
import hu.szacskesz.mobile.tasklist.core.data.TaskDataSource
import hu.szacskesz.mobile.tasklist.core.domain.*
import hu.szacskesz.mobile.tasklist.framework.db.AppDatabase
import hu.szacskesz.mobile.tasklist.framework.db.converters.toDto
import hu.szacskesz.mobile.tasklist.framework.db.converters.toEntity
import java.util.*


class RoomTaskDataSource(val context: Context) : TaskDataSource {

    private val taskDao = AppDatabase.getInstance(context).taskDao()

    override fun setAfterTaskNotificationCreated(afterTaskNotificationCreated: ((TaskNotificationWithTask) -> Unit)?) {
        if(afterTaskNotificationCreated != null) {
            taskDao.afterTaskNotificationCreated = { afterTaskNotificationCreated(it.toDto()) }
        } else {
            taskDao.afterTaskNotificationCreated = null
        }
    }

    override fun setAfterTaskNotificationDeleted(afterTaskNotificationDeleted: ((TaskNotificationWithTask) -> Unit)?) {
        if(afterTaskNotificationDeleted != null) {
            taskDao.afterTaskNotificationDeleted = { afterTaskNotificationDeleted(it.toDto()) }
        } else {
            taskDao.afterTaskNotificationDeleted = null
        }
    }

    override suspend fun read(id: Int?, listId: Int?, isFinished: Boolean?): List<Task> {
        return taskDao.read(id, listId, isFinished).map { it.toDto() }
    }

    override suspend fun create(task: Task): Int {
        return taskDao.create( task.toEntity() ).toInt()
    }

    override suspend fun update(task: Task) {
        return taskDao.update( task.toEntity() )
    }

    override suspend fun delete(task: Task) {
        return taskDao.delete( task.toEntity() )
    }



    override suspend fun readWithTaskListName(id: Int?, listId: Int?, isFinished: Boolean?): List<TaskWithTaskListName> {
        return taskDao.readWithTaskList(id, listId, isFinished).map { it.toDto().toTaskWithTaskListName() }
    }

    override suspend fun readWithTaskList(id: Int?, listId: Int?, isFinished: Boolean?): List<TaskWithTaskList> {
        return taskDao.readWithTaskList(id, listId, isFinished).map { it.toDto() }
    }



    override suspend fun getTaskCount(from: Date, to: Date, isOverdue: Boolean): Int {
        return taskDao.getTaskCount(from, to, Date(), isOverdue)
    }



    override suspend fun readWithTaskNotifications(id: Int?, listId: Int?, isFinished: Boolean?) : List<TaskWithTaskNotifications> {
        return taskDao.readWithTaskNotifications(id, listId, isFinished).map { it.toDto() }
    }

    override suspend fun create(taskWithTaskNotifications: TaskWithTaskNotifications): Int {
        return taskDao.create( taskWithTaskNotifications.toEntity() )
    }

    override suspend fun update(taskWithTaskNotifications: TaskWithTaskNotifications) {
        return taskDao.update( taskWithTaskNotifications.toEntity() )
    }

    override suspend fun delete(taskWithTaskNotifications: TaskWithTaskNotifications) {
        return taskDao.delete( taskWithTaskNotifications.toEntity() )
    }



    override suspend fun readByTaskNotificationId(id: Int): Task? {
        return taskDao.getTaskForTaskNotificationId(id)?.toDto()
    }
}
