package hu.szacskesz.mobile.tasklist.core.data

import hu.szacskesz.mobile.tasklist.core.domain.Group

interface GroupDataSource {
    suspend fun create(group: Group)
    suspend fun read() : List<Group>
    suspend fun update(group: Group)
    suspend fun delete(group: Group)
}