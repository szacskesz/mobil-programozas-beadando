package hu.szacskesz.mobile.tasklist.framework.db.entity

import androidx.room.*


@Entity(tableName = "task_list")
data class TaskListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name : String,
)

data class TaskListWithTasksCountEntity(
    @Embedded val taskList: TaskListEntity,
    @ColumnInfo(name = "task_count") val tasksCount: Int,
    @ColumnInfo(name = "overdue_task_count") val overdueTasksCount: Int,
)
