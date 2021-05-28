package hu.szacskesz.mobile.tasklist.framework.db.entity

import androidx.room.*
import java.util.*

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "description") val description : String,
    @ColumnInfo(name = "done") val done : Boolean,
    @ColumnInfo(name = "deadline", ) val deadline : Date?,
    @ColumnInfo(name = "list_id", ) val listId : Int?,
)

data class TaskWithTaskListEntity(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "list_id",
        entityColumn = "id"
    )
    val list: TaskListEntity?
)