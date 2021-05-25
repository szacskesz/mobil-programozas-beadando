package hu.szacskesz.mobile.tasklist.tasklists

import android.app.Application
import androidx.lifecycle.MutableLiveData
import hu.szacskesz.mobile.tasklist.common.CommonViewModel
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.framework.Interactors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskListsViewModel(application: Application, interactors: Interactors) : CommonViewModel(application, interactors) {

    val taskLists: MutableLiveData<List<TaskList>> = MutableLiveData()

    fun readTaskLists() {
        GlobalScope.launch {
            val docs = interactors.readTaskLists()
            taskLists.postValue(docs)
        }
    }

    fun createTaskList(taskList: TaskList) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.createTaskList(taskList)
            }
            readTaskLists()
        }
    }

    fun updateTaskList(taskList: TaskList) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.updateTaskList(taskList)
            }
            readTaskLists()
        }
    }

    fun deleteTaskList(taskList: TaskList) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.deleteTaskList(taskList)
            }
            readTaskLists()
        }
    }
}