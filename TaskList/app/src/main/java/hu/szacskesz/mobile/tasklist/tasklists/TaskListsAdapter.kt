package hu.szacskesz.mobile.tasklist.tasklists

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.core.domain.TaskListWithTasksCount
import kotlinx.android.synthetic.main.task_lists_item.view.*


class TaskListsAdapter (
    private val taskLists: MutableList<TaskListWithTasksCount> = mutableListOf(),
    private val onOpenClicked: (TaskListWithTasksCount) -> Unit,
    private val onEditClicked: (TaskListWithTasksCount) -> Unit,
    private val onDeleteClicked: (TaskListWithTasksCount) -> Unit,
) : RecyclerView.Adapter<TaskListsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val nameTextView: TextView = view.task_lists_item_name
        val tasksTextView: TextView = view.task_lists_item_tasks
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.task_lists_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        val taskList = taskLists[position]

        viewHolder.nameTextView.text = taskList.name

        val tasksText = SpannableStringBuilder()
        if(taskList.tasksCount > 0) {
            tasksText.color(ContextCompat.getColor(context, R.color.alert_blue))
            { append(context.getString(R.string.task_lists_item_tasks_count, taskList.tasksCount)) }

            if(taskList.overdueTasksCount > 0) {
                tasksText.append(" ")
                tasksText.color(ContextCompat.getColor(context, R.color.alert_red))
                { append(context.getString(R.string.task_lists_item_overdue_tasks_count, taskList.overdueTasksCount)) }
            }
        } else {
            tasksText.color(ContextCompat.getColor(context, R.color.gray))
            { append(context.getString(R.string.task_lists_item_tasks_count_zero)) }
        }
        viewHolder.tasksTextView.text = tasksText

        //listeners
        viewHolder.itemView.setOnClickListener { onOpenClicked(taskList) }
        viewHolder.itemView.task_lists_item_edit_button.setOnClickListener { onEditClicked(taskList) }
        viewHolder.itemView.task_lists_item_delete_button.setOnClickListener { onDeleteClicked(taskList) }
    }

    override fun getItemCount() = taskLists.size

    fun update(newTaskLists: List<TaskListWithTasksCount>) {
        taskLists.clear()
        taskLists.addAll(newTaskLists)

        notifyDataSetChanged()
    }
}
