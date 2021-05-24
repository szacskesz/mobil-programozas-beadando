package hu.szacskesz.mobile.tasklist.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.szacskesz.mobile.tasklist.framework.db.dao.GroupDao
import hu.szacskesz.mobile.tasklist.framework.db.entity.GroupEntity

@Database(
    entities = [GroupEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskListDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "app.db"
        private var instance: TaskListDatabase? = null

        private fun create(context: Context) : TaskListDatabase =
            Room.databaseBuilder(context, TaskListDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(context: Context) : TaskListDatabase = (instance ?: create(context)).also { instance = it }
    }

    abstract fun groupDao() : GroupDao
}