package hu.szacskesz.mobile.tasklist.tasklists

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.szacskesz.mobile.tasklist.R


class TaskListsDeleteDialogFragment : DialogFragment() {
    private var result: Boolean = false
    private var onClosedListener: ((Boolean) -> Unit)? = null

    fun setOnClosedListener(listener: (Boolean) -> Unit): TaskListsDeleteDialogFragment {
        this.onClosedListener = listener

        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.task_lists_delete_dialog_title))
                .setMessage(getString(R.string.task_lists_delete_dialog_message))
                .setPositiveButton(getString(R.string.task_lists_delete_dialog_yes_button))
                    { _, _ -> result = true }
                .setNegativeButton(getString(R.string.task_lists_delete_dialog_no_button), null)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        onClosedListener?.invoke(result)
    }
}