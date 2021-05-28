package hu.szacskesz.mobile.tasklist.tasks

import android.app.Application
import androidx.lifecycle.MutableLiveData
import hu.szacskesz.mobile.tasklist.common.CommonViewModel
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskListName
import hu.szacskesz.mobile.tasklist.framework.Interactors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksViewModel(application: Application, interactors: Interactors) : CommonViewModel(application, interactors) {

    val tasks: MutableLiveData<List<TaskWithTaskListName>> = MutableLiveData()

    fun read() {
        GlobalScope.launch {
            val docs = interactors.readTaskWithTaskListNames()
            tasks.postValue(docs)
        }
    }

    fun create(task: Task) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.createTask(task)
            }
            read()
        }
    }

    fun update(task: Task) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.updateTask(task)
            }
            read()
        }
    }

    fun delete(task: Task) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.deleteTask(task)
            }
            read()
        }
    }
}