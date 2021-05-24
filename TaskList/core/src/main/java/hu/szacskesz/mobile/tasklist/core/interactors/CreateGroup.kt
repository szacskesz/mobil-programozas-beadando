package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.GroupRepository
import hu.szacskesz.mobile.tasklist.core.domain.Group

class CreateGroup(private val repository: GroupRepository) {
    suspend operator fun invoke(group: Group) = repository.create(group)
}