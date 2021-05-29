package hu.szacskesz.mobile.tasklist.tasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.utils.Constants
import kotlinx.android.synthetic.main.tasks_editor_activity.*


class TasksEditorActivity : BaseLanguageAwareActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_editor_activity)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val taskLists: List<TaskList> = intent.getParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS)!!
        var selectedTaskListId: Int  = intent.getIntExtra(Constants.IntentExtra.Key.SELECTED_TASK_LIST_ID, Constants.TaskList.NONE.id)
        if(selectedTaskListId == Constants.TaskList.ALL.id || selectedTaskListId == Constants.TaskList.FINISHED.id) {
            selectedTaskListId = Constants.TaskList.NONE.id
        }
        val taskToUpdate: Task? = intent.getParcelableExtra(Constants.IntentExtra.Key.TASK_TO_UPDATE)

        tasks_editor_activity_title.text = getString(
            if(taskToUpdate == null) R.string.tasks_editor_create_activity_title
            else R.string.tasks_editor_update_activity_title
        )

        tasks_editor_save_button.setOnClickListener {
            val isFormValid = tasks_editor_field_description.text.isNotEmpty()

            if(isFormValid) {
                val listId = (tasks_editor_field_list_dropdown.selectedItem as TaskList).id
                setResult(
                    Activity.RESULT_OK,
                    Intent()
                        .putExtra(Constants.IntentExtra.Key.ACTION,
                            if(taskToUpdate == null) Constants.IntentExtra.Value.CREATE_ACTION
                            else Constants.IntentExtra.Value.UPDATE_ACTION
                        )
                        .putExtra(Constants.IntentExtra.Key.TASK, Task(
                            id = taskToUpdate?.id ?: 0,
                            description = tasks_editor_field_description.text.toString(),
                            done = tasks_editor_field_done.isChecked,
                            deadline = null, //TODO get deadline value from the form
                            listId = if(listId == Constants.TaskList.NONE.id) null else listId
                        ))
                )
                finish()
            }

        }
        tasks_editor_delete_button.visibility = if(taskToUpdate == null) View.GONE else View.VISIBLE
        tasks_editor_delete_button.setOnClickListener {
            TasksDeleteDialogFragment()
                .setOnClosedListener { result ->
                    if(result) {
                        setResult(
                            Activity.RESULT_OK,
                            Intent()
                                .putExtra(Constants.IntentExtra.Key.ACTION, Constants.IntentExtra.Value.DELETE_ACTION)
                                .putExtra(Constants.IntentExtra.Key.TASK, taskToUpdate!!)
                        )
                        finish()
                    }
                }
                .show(supportFragmentManager, null)
        }
        tasks_editor_field_description.setText(taskToUpdate?.description ?: "")
        tasks_editor_field_done.isChecked = taskToUpdate?.done ?: false
        tasks_editor_field_list_dropdown

        val adapterTaskLists = mutableListOf<TaskList>().apply {
            add(TaskList(id = Constants.TaskList.NONE.id, name = getString(Constants.TaskList.NONE.name)))
            addAll(taskLists)
        }.toList()
        val spinnerAdapter = TasksSpinnerAdapter(this, R.layout.default_spinner_dropdown_item, adapterTaskLists)
        spinnerAdapter.setDropDownViewResource(R.layout.default_spinner_dropdown_item)
        tasks_editor_field_list_dropdown.adapter = spinnerAdapter

        tasks_editor_field_list_dropdown.setSelection(adapterTaskLists.map { item -> item.id }.indexOf(
            if(taskToUpdate == null) selectedTaskListId
            else taskToUpdate.listId
        ))

        //TODO set due date + time values
//        TODO date + time picker : due
//        val datePicker =
//            MaterialDatePicker.Builder.datePicker()
//                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//                .setTitleText("Select date")
//                .build()
//        datePicker.show(supportFragmentManager, "tag");






        tasks_editor_field_list_label.text = getString(
            if(taskToUpdate == null) R.string.tasks_editor_create_field_list_label
            else R.string.tasks_editor_update_field_list_label
        )
    }
}