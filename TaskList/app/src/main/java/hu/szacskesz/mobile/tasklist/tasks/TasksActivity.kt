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
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.converters.toTask
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.settings.SettingsActivity
import hu.szacskesz.mobile.tasklist.tasklists.TaskListsActivity
import hu.szacskesz.mobile.tasklist.utils.Constants
import hu.szacskesz.mobile.tasklist.viewmodels.TaskListViewModel
import hu.szacskesz.mobile.tasklist.viewmodels.TaskWithTaskListNameViewModel
import kotlinx.android.synthetic.main.tasks_activity.*


class TasksActivity : BaseLanguageAwareActivity() {
    private var selectedTaskListId: Int = 0
    private lateinit var taskListViewModel: TaskListViewModel
    private lateinit var taskWithTaskListNameViewModel: TaskWithTaskListNameViewModel
    private lateinit var taskListsActivityResult: ActivityResultLauncher<Intent>
    private lateinit var tasksEditorActivityResult: ActivityResultLauncher<Intent>

    private fun addConstantTaskLists(list: List<TaskList>?): List<TaskList>? {
        return if(list == null) null else mutableListOf<TaskList>().apply {
            add(TaskList(id = Constants.TaskList.ALL.id, name = getString(Constants.TaskList.ALL.name)))
            addAll(list)
            add(TaskList(id = Constants.TaskList.FINISHED.id, name = getString(Constants.TaskList.FINISHED.name)))
        }.toList()
    }

    private fun refreshModels() {
        taskListViewModel.read()

        val listId = if(selectedTaskListId == Constants.TaskList.ALL.id
            || selectedTaskListId == Constants.TaskList.NONE.id
            || selectedTaskListId == Constants.TaskList.FINISHED.id) null
            else selectedTaskListId
        val isFinished = selectedTaskListId == Constants.TaskList.FINISHED.id
        taskWithTaskListNameViewModel.read(listId, isFinished)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_activity)

        setSupportActionBar(findViewById(R.id.toolbar))

        selectedTaskListId = Integer.parseInt(PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString(getString(R.string.settings_default_task_list_key), Constants.TaskList.ALL.id.toString())!!)

        taskListViewModel = ViewModelProvider(this, CommonViewModelFactory).get(TaskListViewModel::class.java)
        taskWithTaskListNameViewModel = ViewModelProvider(this, CommonViewModelFactory).get(TaskWithTaskListNameViewModel::class.java)

        taskListViewModel.taskLists.observe(this, {
            val tasksList = this.addConstantTaskLists(it)!!
            val spinnerAdapter = TasksSpinnerAdapter(this, R.layout.tasks_spinner_dropdown_item, tasksList)
            spinnerAdapter.setDropDownViewResource(R.layout.tasks_spinner_dropdown_item)

            task_list_dropdown.adapter = spinnerAdapter
            task_list_dropdown.setSelection(tasksList.map { item -> item.id }.indexOf(selectedTaskListId))
        })
        task_list_dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = adapterView.getItemAtPosition(position) as TaskList
                val shouldRefresh = item.id != selectedTaskListId

                selectedTaskListId = item.id

                if(shouldRefresh) refreshModels()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        val adapter = TasksAdapter(
            onRowClicked = {
                tasksEditorActivityResult.launch(
                    Intent(this, TasksEditorActivity::class.java)
                        .putParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS, ArrayList(taskListViewModel.taskLists.value))
                        .putExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, selectedTaskListId)
                        .putExtra(Constants.IntentExtra.Key.TASK_TO_UPDATE, it.toTask())
                )
            },
            onCheckboxClicked = {
                taskWithTaskListNameViewModel.update(it.copy(done = !it.done).toTask())
            }
        )
        tasks_recycler_view.adapter  = adapter

        taskWithTaskListNameViewModel.taskWithTaskListNames.observe(this, {
            adapter.update(it)
        })

        tasks_item_add_button.setOnClickListener {
            tasksEditorActivityResult.launch(
                Intent(this, TasksEditorActivity::class.java)
                    .putParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS, ArrayList(taskListViewModel.taskLists.value))
                    .putExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, selectedTaskListId)
            )
        }

        tasksEditorActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            val data = it.data

            if (resultCode == Activity.RESULT_OK && data != null) {
                val action = data.getStringExtra(Constants.IntentExtra.Key.ACTION)
                val task = data.getParcelableExtra<Task>(Constants.IntentExtra.Key.TASK)!!

                when(action) {
                    Constants.IntentExtra.Value.CREATE_ACTION -> {
                        taskWithTaskListNameViewModel.create(task)
                    }
                    Constants.IntentExtra.Value.UPDATE_ACTION -> {
                        taskWithTaskListNameViewModel.update(task)
                    }
                    Constants.IntentExtra.Value.DELETE_ACTION -> {
                        taskWithTaskListNameViewModel.delete(task)
                    }
                    else -> throw IllegalStateException("Action value must be one of Constants.IntentExtra.value.*")
                }
            }

            refreshModels()
        }

        taskListsActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            val data = it.data

            if (resultCode == Activity.RESULT_OK && data != null) {
                selectedTaskListId = data.getIntExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, selectedTaskListId)
            }

            refreshModels()
        }

        refreshModels()
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
                        .putParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS, ArrayList(taskListViewModel.taskLists.value))
                )
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
