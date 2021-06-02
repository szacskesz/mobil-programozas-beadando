package hu.szacskesz.mobile.tasklist.framework.db.dao

import androidx.room.*
import hu.szacskesz.mobile.tasklist.framework.db.entity.*
import java.util.*


@Dao
abstract class TaskDao {

    public var afterTaskNotificationCreated: ((TaskNotificationWithTaskEntity) -> Unit)? = null
    public var afterTaskNotificationDeleted: ((TaskNotificationWithTaskEntity) -> Unit)? = null

    private fun invokeCallbacks(created: List<TaskNotificationWithTaskEntity>, deleted: List<TaskNotificationWithTaskEntity>) {
        afterTaskNotificationCreated?.let { cb -> created.forEach { cb(it) } }
        afterTaskNotificationDeleted?.let { cb -> deleted.forEach { cb(it) } }
    }

    @Transaction
    @Query("""
        SELECT * FROM `task` WHERE
            (:id IS NUll OR `task`.id = :id) AND
            (:listId IS NUll OR `task`.list_id = :listId) AND
            (:isFinished IS NULL OR `task`.done = :isFinished)
    """)
    abstract suspend fun read(id: Int?, listId: Int?, isFinished: Boolean?) : List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun create(task: TaskEntity): Long

    @Update(onConflict = OnConflictStrategy.ABORT)
    abstract fun update(task: TaskEntity)

    @Delete
    abstract suspend fun delete(task: TaskEntity)



    @Transaction
    @Query("""
        SELECT * FROM `task` WHERE
            (:id IS NUll OR `task`.id = :id) AND
            (:listId IS NUll OR `task`.list_id = :listId) AND
            (:isFinished IS NULL OR `task`.done = :isFinished)
    """)
    abstract suspend fun readWithTaskList(id: Int?, listId: Int?, isFinished: Boolean?) : List<TaskWithTaskListEntity>



    @Query("""
        SELECT COUNT(*) FROM `task`
        WHERE deadline IS NOT NULL
        AND deadline <= :to
        AND deadline >= :from
        AND (NOT :isOverdue OR deadline <= :now)
    """)
    abstract suspend fun getTaskCount(from: Date, to: Date, now: Date, isOverdue: Boolean) : Int



    @Transaction
    @Query("""
        SELECT * FROM `task` WHERE
            (:id IS NUll OR `task`.id = :id) AND
            (:listId IS NUll OR `task`.list_id = :listId) AND
            (:isFinished IS NULL OR `task`.done = :isFinished)
    """)
    abstract suspend fun readWithTaskNotifications(id: Int?, listId: Int?, isFinished: Boolean?) : List<TaskWithTaskNotificationsEntity>

    @Transaction
    open suspend fun create(taskWithTaskNotifications: TaskWithTaskNotificationsEntity): Int {
        val id = create(taskWithTaskNotifications.task).toInt()
        createNotifications(taskWithTaskNotifications.notifications.map { it.copy(id = 0, taskId = id) })

        val createdNotifications = readNotificationsByTaskId(id).map {
            TaskNotificationWithTaskEntity(task = taskWithTaskNotifications.task.copy(id = id), notification = it)
        }
        val deletedNotifications = listOf<TaskNotificationWithTaskEntity>()
        invokeCallbacks(createdNotifications, deletedNotifications)

        return id
    }

    @Transaction
    open suspend fun update(taskWithTaskNotifications: TaskWithTaskNotificationsEntity) {
        update(taskWithTaskNotifications.task)

        val deletedNotifications = readNotificationsByTaskId(taskWithTaskNotifications.task.id).map {
            TaskNotificationWithTaskEntity(task = taskWithTaskNotifications.task, notification = it)
        }

        //TODO delete only the deleted ones (dont recreate everything)
        deleteNotificationsByTaskId(taskWithTaskNotifications.task.id)
        createNotifications(taskWithTaskNotifications.notifications.map { it.copy(id = 0, taskId = taskWithTaskNotifications.task.id)  })

        val createdNotifications = readNotificationsByTaskId(taskWithTaskNotifications.task.id).map {
            TaskNotificationWithTaskEntity(task = taskWithTaskNotifications.task, notification = it)
        }

        invokeCallbacks(createdNotifications, deletedNotifications)
    }

    @Transaction
    open suspend fun delete(taskWithTaskNotifications: TaskWithTaskNotificationsEntity) {
        val deletedNotifications = readNotificationsByTaskId(taskWithTaskNotifications.task.id).map {
            TaskNotificationWithTaskEntity(task = taskWithTaskNotifications.task, notification = it)
        }

        delete(taskWithTaskNotifications.task)
        //notifications are deleted automatically when task is deleted, because of the cascade on delete

        val createdNotifications = listOf<TaskNotificationWithTaskEntity>()
        invokeCallbacks(createdNotifications, deletedNotifications)
    }



    @Transaction
    @Query(
        """
        SELECT `task`.* FROM `task`
            INNER JOIN `task_notification` ON `task`.id = `task_notification`.task_id
            WHERE `task_notification`.id = :id
    """
    )
    abstract suspend fun getTaskForTaskNotificationId(id: Int): TaskEntity?



    //Not public methods

    @Query("SELECT * FROM 'task_notification' WHERE task_id = :taskId")
    protected abstract suspend fun readNotificationsByTaskId(taskId: Int): List<TaskNotificationEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun createNotifications(taskNotifications: List<TaskNotificationEntity>)

    @Query("DELETE FROM 'task_notification' WHERE task_id = :taskId")
    protected abstract suspend fun deleteNotificationsByTaskId(taskId: Int)
}
