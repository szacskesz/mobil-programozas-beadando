package hu.szacskesz.mobile.tasklist.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import hu.szacskesz.mobile.tasklist.common.CommonViewModel
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskListWithTasksCount
import hu.szacskesz.mobile.tasklist.framework.Interactors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskListWithTasksCountViewModel(application: Application, interactors: Interactors) : CommonViewModel(application, interactors) {

    val taskListWithTasksCounts: MutableLiveData<List<TaskListWithTasksCount>> = MutableLiveData()

    private var id: Int? = null

    fun read(id: Int?) {
        this.id = id

        GlobalScope.launch {
            val docs = interactors.readTaskListWithTasksCounts(id)
            taskListWithTasksCounts.postValue(docs)
        }
    }

    fun create(taskList: TaskList) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.createTaskList(taskList)
            }

            read(id)
        }
    }

    fun update(taskList: TaskList) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.updateTaskList(taskList)
            }

            read(id)
        }
    }

    fun delete(taskList: TaskList) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.deleteTaskList(taskList)
            }

            read(id)
        }
    }
}
