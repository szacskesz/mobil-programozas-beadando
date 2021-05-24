package hu.szacskesz.mobile.tasklist.framework.db.dao

import androidx.room.*
import hu.szacskesz.mobile.tasklist.framework.db.entity.GroupEntity

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(group: GroupEntity)

    @Query("SELECT * FROM `group`")
    suspend fun read() : List<GroupEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(group: GroupEntity)

    @Delete
    suspend fun delete(group: GroupEntity)
}