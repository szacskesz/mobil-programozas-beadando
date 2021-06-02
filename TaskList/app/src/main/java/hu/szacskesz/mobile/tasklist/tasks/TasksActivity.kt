package hu.szacskesz.mobile.tasklist.tasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.TaskListApplication
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications
import hu.szacskesz.mobile.tasklist.settings.SettingsActivity
import hu.szacskesz.mobile.tasklist.tasklists.TaskListsActivity
import hu.szacskesz.mobile.tasklist.utils.Constants
import kotlinx.android.synthetic.main.tasks_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TasksActivity : BaseLanguageAwareActivity() {
    private lateinit var viewModel: TasksViewModel
    private lateinit var taskListsActivityResult: ActivityResultLauncher<Intent>
    private lateinit var tasksEditorActivityResult: ActivityResultLauncher<Intent>

    private fun addConstantTaskLists(list: List<TaskList>?): List<TaskList>? {
        return if(list == null) null else mutableListOf<TaskList>().apply {
            add(TaskList(id = Constants.TaskList.ALL.id, name = getString(Constants.TaskList.ALL.name)))
            addAll(list)
            add(TaskList(id = Constants.TaskList.FINISHED.id, name = getString(Constants.TaskList.FINISHED.name)))
        }.toList()
    }

    private fun refreshViewModel() {
        viewModel.taskLists.read(null)

        val listId = if(viewModel.taskLists.selectedId.value == Constants.TaskList.ALL.id
            || viewModel.taskLists.selectedId.value == Constants.TaskList.NONE.id
            || viewModel.taskLists.selectedId.value == Constants.TaskList.FINISHED.id) null
            else viewModel.taskLists.selectedId.value
        val isFinished = viewModel.taskLists.selectedId.value == Constants.TaskList.FINISHED.id
        viewModel.taskWithTaskListNames.read(null, listId, isFinished)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_activity)

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(this, CommonViewModelFactory).get(TasksViewModel::class.java)
        viewModel.taskLists.selectedId.postValue(
            Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString(getString(R.string.settings_default_task_list_key), Constants.TaskList.ALL.id.toString())!!)
        )

        viewModel.taskLists.data.observe(this, {
            val tasksList = this.addConstantTaskLists(it)!!
            val spinnerAdapter = TasksSpinnerAdapter(this, R.layout.tasks_spinner_dropdown_item, tasksList)
            spinnerAdapter.setDropDownViewResource(R.layout.tasks_spinner_dropdown_item)

            task_list_dropdown.adapter = spinnerAdapter
            task_list_dropdown.setSelection(tasksList.map { item -> item.id }.indexOf(viewModel.taskLists.selectedId.value))
        })
        task_list_dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = adapterView.getItemAtPosition(position) as TaskList
                if(item.id != viewModel.taskLists.selectedId.value) {
                    viewModel.taskLists.selectedId.postValue(item.id)
                    refreshViewModel()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        val adapter = TasksAdapter(
            onRowClicked = {
                val ctx = this

                GlobalScope.launch {
                    val taskWithTaskNotifications = (ctx.application as TaskListApplication)
                        .taskRepository.readWithTaskNotifications(it.id, null, null).first()

                    tasksEditorActivityResult.launch(
                        Intent(ctx, TasksEditorActivity::class.java)
                            .putParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS, ArrayList(viewModel.taskLists.data.value))
                            .putExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, viewModel.taskLists.selectedId.value)
                            .putExtra(Constants.IntentExtra.Key.TASK_WITH_TASK_NOTIFCATIONS_TO_UPDATE, taskWithTaskNotifications.copy())
                    )
                }
            },
            onCheckboxClicked = {
                viewModel.taskWithTaskListNames.update(it.copy(done = !it.done))
            }
        )
        tasks_recycler_view.adapter = adapter

        viewModel.taskWithTaskListNames.data.observe(this, {
            adapter.update(it)
        })

        tasks_item_add_button.setOnClickListener {
            tasksEditorActivityResult.launch(
                Intent(this, TasksEditorActivity::class.java)
                    .putParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS, ArrayList(viewModel.taskLists.data.value))
                    .putExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, viewModel.taskLists.selectedId.value)
            )
        }

        tasksEditorActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            val data = it.data

            if (resultCode == Activity.RESULT_OK && data != null) {
                val action = data.getStringExtra(Constants.IntentExtra.Key.ACTION)
                val taskWithTaskNotifications = data.getParcelableExtra<TaskWithTaskNotifications>(
                    Constants.IntentExtra.Key.TASK_WITH_TASK_NOTIFCATIONS_TO_UPDATE
                )!!

                when(action) {
                    Constants.IntentExtra.Value.CREATE_ACTION -> {
                        viewModel.taskWithTaskListNames.create(taskWithTaskNotifications)
                    }
                    Constants.IntentExtra.Value.UPDATE_ACTION -> {
                        viewModel.taskWithTaskListNames.update(taskWithTaskNotifications)
                    }
                    Constants.IntentExtra.Value.DELETE_ACTION -> {
                        viewModel.taskWithTaskListNames.delete(taskWithTaskNotifications)
                    }
                    else -> throw IllegalStateException("Action value must be one of Constants.IntentExtra.value.*")
                }
            }

            refreshViewModel()
        }

        taskListsActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            val data = it.data

            if (resultCode == Activity.RESULT_OK && data != null) {
                val id = data.getIntExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, viewModel.taskLists.selectedId.value!!)
                if(id != viewModel.taskLists.selectedId.value) {
                    viewModel.taskLists.selectedId.postValue(id)
                }
            }

            refreshViewModel()
        }

        refreshViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_task_lists -> {
                taskListsActivityResult.launch(
                    Intent(this, TaskListsActivity::class.java)
                )
            }
            R.id.menu_action_settings -> {
                this.startActivity(
                    Intent(this, SettingsActivity::class.java)
                        .putParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS, ArrayList(viewModel.taskLists.data.value))
                )
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
