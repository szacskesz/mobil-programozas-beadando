package hu.szacskesz.mobile.tasklist.tasks

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.lifecycle.MutableLiveData
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotification
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskNotifications
import hu.szacskesz.mobile.tasklist.utils.Constants
import kotlinx.android.synthetic.main.tasks_editor_activity.*
import java.util.*


class TasksEditorActivity : BaseLanguageAwareActivity() {

    private var deadline: Calendar? = null

    private var notifications: MutableLiveData<List<TaskNotification>> = MutableLiveData()

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
        val taskWithTaskNotificationsToUpdate: TaskWithTaskNotifications? = intent.getParcelableExtra(Constants.IntentExtra.Key.TASK_WITH_TASK_NOTIFCATIONS_TO_UPDATE)

        tasks_editor_activity_title.text = getString(
            if(taskWithTaskNotificationsToUpdate == null) R.string.tasks_editor_create_activity_title
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
                            if(taskWithTaskNotificationsToUpdate == null) Constants.IntentExtra.Value.CREATE_ACTION
                            else Constants.IntentExtra.Value.UPDATE_ACTION
                        )
                        .putExtra(Constants.IntentExtra.Key.TASK_WITH_TASK_NOTIFCATIONS_TO_UPDATE, TaskWithTaskNotifications(
                            id = taskWithTaskNotificationsToUpdate?.id ?: 0,
                            description = tasks_editor_field_description.text.toString(),
                            done = tasks_editor_field_done.isChecked,
                            deadline = deadline?.time,
                            listId = if(listId == Constants.TaskList.NONE.id) null else listId,
                            notifications = notifications.value!!
                        ))
                )
                finish()
            }

        }
        tasks_editor_delete_button.visibility = if(taskWithTaskNotificationsToUpdate == null) View.GONE else View.VISIBLE
        tasks_editor_delete_button.setOnClickListener {
            TasksDeleteDialogFragment()
                .setOnClosedListener { result ->
                    if(result) {
                        setResult(
                            Activity.RESULT_OK,
                            Intent()
                                .putExtra(Constants.IntentExtra.Key.ACTION, Constants.IntentExtra.Value.DELETE_ACTION)
                                .putExtra(Constants.IntentExtra.Key.TASK_WITH_TASK_NOTIFCATIONS_TO_UPDATE, taskWithTaskNotificationsToUpdate!!)
                        )
                        finish()
                    }
                }
                .show(supportFragmentManager, null)
        }
        tasks_editor_field_description.setText(taskWithTaskNotificationsToUpdate?.description ?: "")
        tasks_editor_field_done.isChecked = taskWithTaskNotificationsToUpdate?.done ?: false

        val adapterTaskLists = mutableListOf<TaskList>().apply {
            add(TaskList(id = Constants.TaskList.NONE.id, name = getString(Constants.TaskList.NONE.name)))
            addAll(taskLists)
        }.toList()
        val spinnerAdapter = TasksSpinnerAdapter(this, R.layout.default_spinner_dropdown_item, adapterTaskLists)
        spinnerAdapter.setDropDownViewResource(R.layout.default_spinner_dropdown_item)
        tasks_editor_field_list_dropdown.adapter = spinnerAdapter

        tasks_editor_field_list_dropdown.setSelection(adapterTaskLists.map { item -> item.id }.indexOf(
            if(taskWithTaskNotificationsToUpdate == null) selectedTaskListId
            else taskWithTaskNotificationsToUpdate.listId
        ))

        createDeadlineDueDateField()

        if(taskWithTaskNotificationsToUpdate != null) {
            if(taskWithTaskNotificationsToUpdate.deadline != null) {
                val cal = Calendar.getInstance()
                cal.time = taskWithTaskNotificationsToUpdate.deadline!!

                deadline = cal
                updateDeadlineFields()
            }
        }

        tasks_editor_field_list_label.text = getString(
            if(taskWithTaskNotificationsToUpdate == null) R.string.tasks_editor_create_field_list_label
            else R.string.tasks_editor_update_field_list_label
        )

        val adapter = TasksNotificationsAdapter(
            onDeleteClicked = { taskNotificationToDelete ->
                notifications.postValue(
                    notifications.value!!.filter { it != taskNotificationToDelete }
                )
            }
        )
        notifications.observe(this) {
            adapter.update(it)
        }
        tasks_editor_field_notifications_recycler_view.adapter  = adapter
        taskWithTaskNotificationsToUpdate?.let { notifications.postValue(it.notifications) }

        tasks_editor_field_notifications_add_button.setOnClickListener {
            createDateTimePickerDialog({
                notifications.postValue(
                    mutableListOf<TaskNotification>().apply {
                        addAll(notifications.value!!)
                        add(TaskNotification(id = 0, datetime = it.time, taskId = taskWithTaskNotificationsToUpdate?.id ?: 0))
                    }
                )
            })
        }
    }

    private fun createDateTimePickerDialog(cb: ((Calendar) -> Unit), currentDateTime: Calendar = Calendar.getInstance()) {
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
                    val pickedDateTime = Calendar.getInstance()
                    pickedDateTime.set(year, month, day, hour, minute)

                    cb(pickedDateTime)
                }, startHour, startMinute, true)
                timePickerDialog.setButton(
                    TimePickerDialog.BUTTON_POSITIVE,
                    getString(R.string.tasks_editor_picker_dialog_yes_button),
                    timePickerDialog
                )
                timePickerDialog.setButton(
                    TimePickerDialog.BUTTON_NEGATIVE,
                    getString(R.string.tasks_editor_picker_dialog_no_button),
                    timePickerDialog
                )
                timePickerDialog.show()
            }, startYear, startMonth, startDay
        )
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_POSITIVE,
            getString(R.string.tasks_editor_picker_dialog_yes_button),
            datePickerDialog
        )
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_NEGATIVE,
            getString(R.string.tasks_editor_picker_dialog_no_button),
            datePickerDialog
        )
        datePickerDialog.show()
    }

    private fun updateDeadlineFields() {
        val calendar = deadline

        if(calendar != null) {
            tasks_editor_field_due_date.setText(
                StringBuilder()
                    .append(DateFormat.getDateFormat(this).format(calendar.time))
                    .append(" ")
                    .append(DateFormat.getTimeFormat(this).format(calendar.time))
                    .toString()
            )
            tasks_editor_field_due_date_cancel.visibility = View.VISIBLE
        } else {
            tasks_editor_field_due_date.setText("")
            tasks_editor_field_due_date_cancel.visibility = View.GONE
        }
    }

    private fun createDeadlineDueDateField() {
        val dueDateOnClickListener: (v: View) -> Unit = {
            val calendar = deadline ?: Calendar.getInstance()
            createDateTimePickerDialog({
                deadline = it
                updateDeadlineFields()
            }, calendar)
        }
        tasks_editor_field_due_date_toggle.setOnClickListener(dueDateOnClickListener)
        tasks_editor_field_due_date.setOnClickListener(dueDateOnClickListener)
        tasks_editor_field_due_date_cancel.setOnClickListener {
            deadline = null
            updateDeadlineFields()
        }
    }
}
