package hu.szacskesz.mobile.tasklist.core.interactors

import hu.szacskesz.mobile.tasklist.core.data.GroupRepository

class ReadGroups(private val repository: GroupRepository) {
    suspend operator fun invoke() = repository.read();
}