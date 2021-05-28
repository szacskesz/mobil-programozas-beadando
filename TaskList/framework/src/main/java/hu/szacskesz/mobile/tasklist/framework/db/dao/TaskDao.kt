package hu.szacskesz.mobile.tasklist.framework.db.dao

import androidx.room.*
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskEntity
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskWithTaskListEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(task: TaskEntity)

    @Transaction
    @Query("SELECT * FROM `task`")
    suspend fun read() : List<TaskEntity>

    @Transaction
    @Query("SELECT * FROM `task`")
    suspend fun readWithTaskListEntity() : List<TaskWithTaskListEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}