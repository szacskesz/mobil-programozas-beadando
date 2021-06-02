package hu.szacskesz.mobile.tasklist.tasks

import android.app.Application
import androidx.lifecycle.MutableLiveData
import hu.szacskesz.mobile.tasklist.common.CommonViewModel
import hu.szacskesz.mobile.tasklist.core.converters.toTask
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskListName
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications
import hu.szacskesz.mobile.tasklist.framework.Interactors
import hu.szacskesz.mobile.tasklist.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TasksViewModel(application: Application, interactors: Interactors) : CommonViewModel(application, interactors) {
    val taskWithTaskListNames = TaskWithTaskListNames()
    inner class TaskWithTaskListNames {
        val data: MutableLiveData<List<TaskWithTaskListName>> = MutableLiveData()

        private var id: Int? = null
        private var listId: Int? = null
        private var isFinished: Boolean? = null

        fun read(id: Int?, listId: Int?, isFinished: Boolean?) {
            this.id = id
            this.listId = listId
            this.isFinished = isFinished

            GlobalScope.launch {
                val docs = interactors.readTaskWithTaskListNames(id, listId, isFinished)
                data.postValue(docs)
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

        fun create(taskWithTaskListName: TaskWithTaskListName) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.createTask(taskWithTaskListName.toTask())
                }

                read(id, listId, isFinished)
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

        fun update(task: Task) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.updateTask(task)
                }

                read(id, listId, isFinished)
            }
        }

        fun update(taskWithTaskListName: TaskWithTaskListName) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.updateTask(taskWithTaskListName.toTask())
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

        fun delete(task: Task) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.deleteTask(task)
                }

                read(id, listId, isFinished)
            }
        }

        fun delete(taskWithTaskListName: TaskWithTaskListName) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.deleteTask(taskWithTaskListName.toTask())
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

    val taskLists = TaskLists()
    inner class TaskLists {
        var selectedId: MutableLiveData<Int> = MutableLiveData(Constants.TaskList.ALL.id)
        val data: MutableLiveData<List<TaskList>> = MutableLiveData()

        private var id: Int? = null

        fun read(id: Int?) {
            this.id = id

            GlobalScope.launch {
                val docs = interactors.readTaskLists(id)
                data.postValue(docs)
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
}
