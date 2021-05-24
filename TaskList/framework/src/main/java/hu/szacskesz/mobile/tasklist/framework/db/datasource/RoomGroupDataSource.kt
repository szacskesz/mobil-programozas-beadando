package hu.szacskesz.mobile.tasklist.framework.db.datasource

import android.content.Context
import hu.szacskesz.mobile.tasklist.core.data.GroupDataSource
import hu.szacskesz.mobile.tasklist.core.domain.Group
import hu.szacskesz.mobile.tasklist.framework.db.TaskListDatabase
import hu.szacskesz.mobile.tasklist.framework.db.entity.GroupEntity

class RoomGroupDataSource(val context: Context) : GroupDataSource {

    private val groupDao = TaskListDatabase.getInstance(context).groupDao()

    override suspend fun create(group: Group) {
        return groupDao.create( GroupEntity(group.id, group.name) )
    }

    override suspend fun read(): List<Group> {
        return groupDao.read().map {
            Group(it.id, it.name)
        }
    }

    override suspend fun update(group: Group)  {
        return groupDao.create( GroupEntity(group.id, group.name) )
    }

    override suspend fun delete(group: Group) {
        return groupDao.delete( GroupEntity(group.id, group.name) )
    }
}