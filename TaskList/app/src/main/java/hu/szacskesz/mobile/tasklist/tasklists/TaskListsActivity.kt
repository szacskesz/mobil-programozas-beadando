package hu.szacskesz.mobile.tasklist.tasklists

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.converters.toTaskList
import hu.szacskesz.mobile.tasklist.utils.Constants
import hu.szacskesz.mobile.tasklist.viewmodels.TaskListWithTasksCountViewModel
import kotlinx.android.synthetic.main.task_lists_activity.*


class TaskListsActivity : BaseLanguageAwareActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_lists_activity)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModel: TaskListWithTasksCountViewModel = ViewModelProvider(this, CommonViewModelFactory).get(TaskListWithTasksCountViewModel::class.java)

        val adapter = TaskListsAdapter(
            onOpenClicked = {
                setResult(Activity.RESULT_OK, Intent().putExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, it.id))
                finish()
            },
            onEditClicked = {
                TaskListsEditorDialogFragment(it.toTaskList())
                    .setOnClosedListener { result ->
                        if(result != null) viewModel.update(result)
                    }
                    .show(supportFragmentManager, null)
            },
            onDeleteClicked = {
                TaskListsDeleteDialogFragment()
                    .setOnClosedListener { result ->
                        if(result) viewModel.delete(it.toTaskList())
                    }
                    .show(supportFragmentManager, null)
            }
        )
        task_lists_recycler_view.adapter  = adapter

        viewModel.taskListWithTasksCounts.observe(this, { adapter.update(it) })
        viewModel.read(null)

        task_lists_item_add_button.setOnClickListener {
            TaskListsEditorDialogFragment()
                .setOnClosedListener { result ->
                    if(result != null) viewModel.create(result)
                }
                .show(supportFragmentManager, null)
        }
    }

}
