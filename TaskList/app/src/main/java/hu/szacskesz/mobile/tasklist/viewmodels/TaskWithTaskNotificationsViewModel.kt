package hu.szacskesz.mobile.tasklist.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import hu.szacskesz.mobile.tasklist.common.CommonViewModel
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications
import hu.szacskesz.mobile.tasklist.framework.Interactors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskWithTaskNotificationsViewModel(application: Application, interactors: Interactors) : CommonViewModel(application, interactors) {

    val taskWithTaskNotifications: MutableLiveData<List<TaskWithTaskNotifications>> = MutableLiveData()

    private var id: Int? = null
    private var listId: Int? = null
    private var isFinished: Boolean? = null

    fun read(id: Int?, listId: Int?, isFinished: Boolean?) {
        this.id = id
        this.listId = listId
        this.isFinished = isFinished

        GlobalScope.launch {
            val docs = interactors.readTaskWithTaskNotifications(id, listId, isFinished)
            taskWithTaskNotifications.postValue(docs)
        }
    }

    fun create(taskWithTaskNotifications: TaskWithTaskNotifications) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.createTaskWithTaskNotifications(taskWithTaskNotifications)
            }

            read(id, listId, isFinished)
        }
    }

    fun update(taskWithTaskNotifications: TaskWithTaskNotifications) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.updateTaskWithTaskNotifications(taskWithTaskNotifications)
            }

            read(id, listId, isFinished)
        }
    }

    fun delete(taskWithTaskNotifications: TaskWithTaskNotifications) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.deleteTaskWithTaskNotifications(taskWithTaskNotifications)
            }

            read(id, listId, isFinished)
        }
    }
}
