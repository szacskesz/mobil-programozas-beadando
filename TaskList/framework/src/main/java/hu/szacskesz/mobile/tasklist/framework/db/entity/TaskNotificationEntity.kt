package hu.szacskesz.mobile.tasklist.framework.db.entity

import androidx.room.*
import java.util.*


@Entity(
    tableName = "task_notification",
    foreignKeys = [
        ForeignKey(entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["task_id"])
    ]
)
data class TaskNotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "datetime") val datetime : Date,
    @ColumnInfo(name = "task_id") val taskId : Int,
)

data class TaskNotificationWithTaskEntity(
    @Embedded val notification: TaskNotificationEntity,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: TaskEntity
)
