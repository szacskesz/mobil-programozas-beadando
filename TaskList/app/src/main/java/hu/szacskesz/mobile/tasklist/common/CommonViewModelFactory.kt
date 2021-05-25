package hu.szacskesz.mobile.tasklist.common

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.szacskesz.mobile.tasklist.framework.Interactors
import java.lang.IllegalStateException

object CommonViewModelFactory : ViewModelProvider.Factory {
    lateinit var application: Application
    lateinit var dependencies: Interactors

    fun inject(application: Application, dependencies: Interactors) {
        CommonViewModelFactory.application = application
        CommonViewModelFactory.dependencies = dependencies
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (CommonViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass.getConstructor(Application::class.java, Interactors::class.java).newInstance(application, dependencies)
        } else {
            throw IllegalStateException("ViewModel must extend CommonViewModel")
        }
    }
}