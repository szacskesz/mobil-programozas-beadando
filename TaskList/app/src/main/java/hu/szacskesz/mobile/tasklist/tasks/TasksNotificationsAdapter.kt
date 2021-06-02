package hu.szacskesz.mobile.tasklist.tasks

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.core.domain.TaskNotification
import kotlinx.android.synthetic.main.tasks_editor_field_notifications_item.view.*


class TasksNotificationsAdapter (
    private val taskNotifications: MutableList<TaskNotification> = mutableListOf(),
    private val onDeleteClicked: (TaskNotification) -> Unit,
) : RecyclerView.Adapter<TasksNotificationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val datetime: TextView = view.tasks_editor_field_notifications_datetime
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.tasks_editor_field_notifications_item,
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        val taskNotification = taskNotifications[position]

        viewHolder.datetime.text = StringBuilder()
            .append(DateFormat.getDateFormat(context).format(taskNotification.datetime))
            .append(" ")
            .append(DateFormat.getTimeFormat(context).format(taskNotification.datetime))
            .toString()

        //listeners
        viewHolder.itemView.tasks_editor_notifications_delete_button.setOnClickListener { onDeleteClicked(taskNotification) }
    }

    override fun getItemCount() = taskNotifications.size

    fun update(newTaskNotifications: List<TaskNotification>) {
        taskNotifications.clear()
        taskNotifications.addAll(newTaskNotifications)

        notifyDataSetChanged()
    }
}
