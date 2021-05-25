package hu.szacskesz.mobile.tasklist.tasklists

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.core.domain.TaskList


class TaskListsEditorDialogFragment(
    private val taskListToUpdate: TaskList? = null
) : DialogFragment() {
    private var onClosedListener: ((TaskList?) -> Unit)? = null

    fun setOnClosedListener(listener: (TaskList?) -> Unit): TaskListsEditorDialogFragment {
        this.onClosedListener = listener

        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.task_lists_editor_dialog, null)
            val nameEditText: EditText = view.findViewById(R.id.task_lists_editor_dialog_field_name)

            val dialog = AlertDialog.Builder(it)
                .setView(view)
                .setTitle(getString(
                    if(taskListToUpdate != null) R.string.task_lists_editor_update_dialog_title
                    else R.string.task_lists_editor_create_dialog_title
                ))
                .setPositiveButton(getString(
                    if(taskListToUpdate != null) R.string.task_lists_editor_update_dialog_yes_button
                    else R.string.task_lists_editor_create_dialog_yes_button
                ), null)
                .setNegativeButton(getString(R.string.task_lists_editor_dialog_no_button), null)
                .create()

            if(taskListToUpdate != null) nameEditText.setText(taskListToUpdate.name)
            nameEditText.requestFocus()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

            dialog.setOnShowListener {
                val yesButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                yesButton.setOnClickListener {
                    val isFormValid = nameEditText.text.isNotEmpty()

                    if(isFormValid) {
                        onClosedListener?.invoke(
                            taskListToUpdate?.copy(name = nameEditText.text.toString())
                                ?: TaskList(name = nameEditText.text.toString())
                        )
                        dialog.dismiss()
                    }
                }

                val noButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                noButton.setOnClickListener {
                    onClosedListener?.invoke(null)
                    dialog.dismiss()
                }
            }

            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}