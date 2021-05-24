package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.Group

class GroupRepository(private val dataSource: GroupDataSource) {
    suspend fun create(group: Group) = dataSource.create(group)
    suspend fun read() = dataSource.read()
    suspend fun update(group: Group) = dataSource.update(group)
    suspend fun delete(group: Group) = dataSource.delete(group)
}