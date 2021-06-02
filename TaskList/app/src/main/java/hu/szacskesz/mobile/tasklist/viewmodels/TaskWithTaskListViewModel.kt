package hu.szacskesz.mobile.tasklist.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import hu.szacskesz.mobile.tasklist.common.CommonViewModel
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskList
import hu.szacskesz.mobile.tasklist.framework.Interactors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskWithTaskListViewModel(application: Application, interactors: Interactors) : CommonViewModel(application, interactors) {

    val taskWithTaskLists: MutableLiveData<List<TaskWithTaskList>> = MutableLiveData()

    private var id: Int? = null
    private var listId: Int? = null
    private var isFinished: Boolean? = null

    fun read(id: Int?, listId: Int?, isFinished: Boolean?) {
        this.id = id
        this.listId = listId
        this.isFinished = isFinished

        GlobalScope.launch {
            val docs = interactors.readTaskWithTaskLists(id, listId, isFinished)
            taskWithTaskLists.postValue(docs)
        }
    }

    fun create(task: Task) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.createTask(task)
            }

            read(id, listId, isFinished)
        }
    }

    fun update(task: Task) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.updateTask(task)
            }

            read(id, listId, isFinished)
        }
    }

    fun delete(task: Task) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.deleteTask(task)
            }

            read(id, listId, isFinished)
        }
    }
}
