package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.GroupRepository
import hu.szacskesz.mobile.tasklist.core.domain.Group

class DeleteGroup(private val repository: GroupRepository) {
    suspend operator fun invoke(group: Group) = repository.delete(group)
}