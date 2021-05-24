package hu.szacskesz.mobile.tasklist.framework.db.dao

import androidx.room.*
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskListEntity

@Dao
interface TaskListDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(taskList: TaskListEntity)

    @Query("SELECT * FROM `task-list`")
    suspend fun read() : List<TaskListEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(taskList: TaskListEntity)

    @Delete
    suspend fun delete(taskList: TaskListEntity)
}