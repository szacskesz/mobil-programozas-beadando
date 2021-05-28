package hu.szacskesz.mobile.tasklist.tasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.common.CommonViewModelFactory
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.settings.SettingsActivity
import hu.szacskesz.mobile.tasklist.tasklists.TaskListsActivity
import hu.szacskesz.mobile.tasklist.utils.Constants
import kotlinx.android.synthetic.main.tasks_activity.*
import java.util.*
import kotlin.collections.ArrayList


class TasksActivity : BaseLanguageAwareActivity() {
    private var selectedTaskListId: MutableLiveData<Int> = MutableLiveData(0)
    private val taskListsResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val resultCode = it.resultCode
        val data = it.data

        if (resultCode == Activity.RESULT_OK && data != null) {
            selectedTaskListId.postValue(
                data.getIntExtra(Constants.IntentExtra.key.SELECTED_TASK_LIST_ID, selectedTaskListId.value!!)
            )
        }
    }

    private fun addConstantTaskLists(list: List<TaskList>?): List<TaskList>? {
        return if(list == null) null else mutableListOf<TaskList>().apply {
            add(TaskList(id = Constants.TaskList.ALL.id, name = getString(Constants.TaskList.ALL.name)))
            addAll(list)
            add(TaskList(id = Constants.TaskList.FINISHED.id, name = getString(Constants.TaskList.FINISHED.name)))
        }.toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_activity)

        setSupportActionBar(findViewById(R.id.toolbar))

        val spinnerViewModel: TasksSpinnerViewModel = ViewModelProviders.of(this, CommonViewModelFactory).get(TasksSpinnerViewModel::class.java)
        selectedTaskListId.observe(this, {
            val tasksList = this.addConstantTaskLists(spinnerViewModel.taskLists.value)
            val newIndex = tasksList?.map { item -> item.id }?.indexOf(selectedTaskListId.value)
            if(newIndex != null) task_list_dropdown.setSelection(newIndex)
        })
        spinnerViewModel.taskLists.observe(this, {
            val tasksList = this.addConstantTaskLists(it)!!
            val spinnerAdapter = TasksSpinnerAdapter(this, R.layout.tasks_spinner_dropdown_item, tasksList)
            spinnerAdapter.setDropDownViewResource(R.layout.tasks_spinner_dropdown_item)

            task_list_dropdown.adapter = spinnerAdapter
            task_list_dropdown.setSelection(tasksList.map { item -> item.id }.indexOf(selectedTaskListId.value))
        })
        task_list_dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = adapterView.getItemAtPosition(position) as TaskList
                selectedTaskListId.postValue(item.id)
                //TODO
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        spinnerViewModel.read()




        val tasksViewModel: TasksViewModel = ViewModelProviders.of(this, CommonViewModelFactory).get(TasksViewModel::class.java)
        val tasksEditorResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            val data = it.data

            if (resultCode == Activity.RESULT_OK && data != null) {
                val action = data.getStringExtra(Constants.IntentExtra.key.ACTION)
                val task = data.getParcelableExtra<Task>(Constants.IntentExtra.key.TASK)!!

                when(action) {
                    Constants.IntentExtra.value.CREATE_ACTION -> {
                        tasksViewModel.create(task)
                    }
                    Constants.IntentExtra.value.UPDATE_ACTION -> {
                        tasksViewModel.update(task)
                    }
                    Constants.IntentExtra.value.DELETE_ACTION -> {
                        tasksViewModel.delete(task)
                    }
                    else -> throw IllegalStateException("Action value must be one of Constants.IntentExtra.value.*")
                }
            }
        }
        val adapter = TasksAdapter(
            onRowClicked = {
                tasksEditorResult.launch(
                    Intent(this, TasksEditorActivity::class.java)
                        .putParcelableArrayListExtra(Constants.IntentExtra.key.TASK_LISTS, ArrayList(spinnerViewModel.taskLists.value))
                        .putExtra(Constants.IntentExtra.key.SELECTED_TASK_LIST_ID, selectedTaskListId.value)
                        .putExtra(Constants.IntentExtra.key.TASK_TO_UPDATE, Task(
                            id = it.id,
                            description = it.description,
                            done = it.done,
                            deadline = it.deadline,
                            listId = it.listId
                        ))
                )
            },
            onCheckboxClicked = {
                tasksViewModel.update(Task(
                    it.id,
                    it.description,
                    !it.done,
                    it.deadline,
                    it.listId
                ))
            }
        )
        tasks_recycler_view.adapter  = adapter

        tasksViewModel.tasks.observe(this, { adapter.update(it) })
        tasksViewModel.read()

        tasks_item_add_button.setOnClickListener {
            tasksEditorResult.launch(
                Intent(this, TasksEditorActivity::class.java)
                    .putParcelableArrayListExtra(Constants.IntentExtra.key.TASK_LISTS, ArrayList(spinnerViewModel.taskLists.value))
                    .putExtra(Constants.IntentExtra.key.SELECTED_TASK_LIST_ID, selectedTaskListId.value)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_task_lists -> {
                taskListsResult.launch(
                    Intent(this, TaskListsActivity::class.java)
                )
            }
            R.id.menu_action_settings -> {
                this.startActivity(Intent(this, SettingsActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
