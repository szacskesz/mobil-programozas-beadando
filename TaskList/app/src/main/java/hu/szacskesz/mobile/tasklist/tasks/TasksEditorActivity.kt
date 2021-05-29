package hu.szacskesz.mobile.tasklist.tasks

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.core.domain.Task
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.utils.Constants
import kotlinx.android.synthetic.main.tasks_editor_activity.*
import java.util.*


class TasksEditorActivity : BaseLanguageAwareActivity() {

    private var deadline: Calendar? = null

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
                            deadline = deadline?.time,
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

        createDeadlineDueDateField()
        createDeadlineDueTimeField()
        if(taskToUpdate != null) {
            if(taskToUpdate.deadline != null) {
                val cal = Calendar.getInstance()
                cal.time = taskToUpdate.deadline!!

                deadline = cal
                updateDeadlineFields()
            }
        }

        tasks_editor_field_list_label.text = getString(
            if(taskToUpdate == null) R.string.tasks_editor_create_field_list_label
            else R.string.tasks_editor_update_field_list_label
        )

        //TODO delete testing
        if(taskToUpdate != null) {
            createNotificationChannel()
            val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Title") //TODO:Feladat határidő: 20:33
                .setContentText("Message") //TODO task.description
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)

            with(NotificationManagerCompat.from(this)) {
                notify(R.integer.notification_task_id, builder.build())
            }
        }
    }

    //TODO delete testing
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.channel_id),
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = getString(R.string.channel_description) }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateDeadlineFields() {
        val calendar = deadline

        if(calendar != null) {
            tasks_editor_field_due_date.setText(DateFormat.getDateFormat(this).format(calendar.time))
            tasks_editor_field_due_time.setText(DateFormat.getTimeFormat(this).format(calendar.time))
            tasks_editor_field_due_date_cancel.visibility = View.VISIBLE
            tasks_editor_field_due_time_cancel.visibility = View.VISIBLE
        } else {
            tasks_editor_field_due_date.setText("")
            tasks_editor_field_due_date_cancel.visibility = View.GONE
            tasks_editor_field_due_time.setText("")
            tasks_editor_field_due_time_cancel.visibility = View.GONE
        }
    }

    private fun createDeadlineDueDateField() {
        val dueDateOnClickListener: (v: View) -> Unit = {
            val calendar = deadline ?: Calendar.getInstance()
            val dialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    deadline = calendar
                    updateDeadlineFields()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                getString(R.string.tasks_editor_picker_dialog_yes_button),
                dialog
            )
            dialog.setButton(
                DatePickerDialog.BUTTON_NEGATIVE,
                getString(R.string.tasks_editor_picker_dialog_no_button),
                dialog
            )
            dialog.show()
        }
        tasks_editor_field_due_date_toggle.setOnClickListener(dueDateOnClickListener)
        tasks_editor_field_due_date.setOnClickListener(dueDateOnClickListener)
        tasks_editor_field_due_date_cancel.setOnClickListener {
            deadline = null
            updateDeadlineFields()
        }
    }

    private fun createDeadlineDueTimeField() {
        val dueTimeOnClickListener: (v: View) -> Unit = {
            val calendar = deadline ?: Calendar.getInstance()
            val dialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    deadline = calendar
                    updateDeadlineFields()
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            dialog.setButton(
                TimePickerDialog.BUTTON_POSITIVE,
                getString(R.string.tasks_editor_picker_dialog_yes_button),
                dialog
            )
            dialog.setButton(
                TimePickerDialog.BUTTON_NEGATIVE,
                getString(R.string.tasks_editor_picker_dialog_no_button),
                dialog
            )
            dialog.show()
        }
        tasks_editor_field_due_time_toggle.setOnClickListener(dueTimeOnClickListener)
        tasks_editor_field_due_time.setOnClickListener(dueTimeOnClickListener)
        tasks_editor_field_due_time_cancel.setOnClickListener {
            deadline = null
            updateDeadlineFields()
        }
    }
}
