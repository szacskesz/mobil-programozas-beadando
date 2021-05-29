package hu.szacskesz.mobile.tasklist.utils

import hu.szacskesz.mobile.tasklist.R

object Constants {
    object TaskList {
        object NONE {
            const val id = -1
            const val name = R.string.task_list_none_name
        }
        object ALL {
            const val id = -2
            const val name = R.string.task_list_all_name
        }
        object FINISHED {
            const val id = -3
            const val name = R.string.task_list_finished_name
        }
    }

    object IntentExtra {
        object Key {
            const val TASK_LISTS = "taskLists"
            const val SELECTED_TASK_LIST_ID = "selectedTaskListId"
            const val TASK_TO_UPDATE = "taskToUpdate"
            const val ACTION = "action"
            const val TASK = "task"
        }
        object Value {
            const val CREATE_ACTION = "create"
            const val UPDATE_ACTION = "update"
            const val DELETE_ACTION = "delete"
        }
    }
}