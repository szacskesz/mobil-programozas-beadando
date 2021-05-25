package hu.szacskesz.mobile.tasklist.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import hu.szacskesz.mobile.tasklist.TaskListApplication
import hu.szacskesz.mobile.tasklist.framework.Interactors

open class CommonViewModel(application: Application, protected val interactors: Interactors) : AndroidViewModel(application) {
    protected val application: TaskListApplication = getApplication()
}