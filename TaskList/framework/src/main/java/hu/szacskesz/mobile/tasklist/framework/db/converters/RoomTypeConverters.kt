package hu.szacskesz.mobile.tasklist.framework.db.converters

import androidx.room.TypeConverter
import java.util.*


class RoomTypeConverters {
    companion object {
        @JvmStatic
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @JvmStatic
        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time?.toLong()
        }
    }
}
