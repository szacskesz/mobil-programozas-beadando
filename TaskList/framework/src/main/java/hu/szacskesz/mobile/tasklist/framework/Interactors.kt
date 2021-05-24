package hu.szacskesz.mobile.tasklist.framework

import hu.szacskesz.mobile.tasklist.core.interactors.*

data class Interactors(
    val createGroup: CreateGroup,
    val readGroups: ReadGroups,
    val updateGroup: UpdateGroup,
    val deleteGroup: DeleteGroup,
)
