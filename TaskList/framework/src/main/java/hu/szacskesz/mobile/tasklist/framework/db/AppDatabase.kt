package hu.szacskesz.mobile.tasklist.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.szacskesz.mobile.tasklist.framework.db.dao.TaskDao
import hu.szacskesz.mobile.tasklist.framework.db.dao.TaskListDao
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskEntity
import hu.szacskesz.mobile.tasklist.framework.db.entity.TaskListEntity
import hu.szacskesz.mobile.tasklist.framework.db.utils.RoomTypeConverters

@Database(
    entities = [
        TaskEntity::class,
        TaskListEntity::class
   ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "app.db"
        private var instance: AppDatabase? = null

        private fun create(context: Context) : AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(context: Context) : AppDatabase = (instance ?: create(context)).also { instance = it }
    }

    abstract fun taskDao() : TaskDao
    abstract fun taskListDao() : TaskListDao
}