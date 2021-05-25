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
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import kotlinx.android.synthetic.main.task_lists_item.view.*


//TODO
data class D (val name: String, val tasksCount: Int, val overdueTasksCount: Int)

class TaskListsAdapter (
    private val taskLists: MutableList<TaskList> = mutableListOf(),
    private val onOpenClicked: (TaskList) -> Unit,
    private val onEditClicked: (TaskList) -> Unit,
    private val onDeleteClicked: (TaskList) -> Unit,
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
        val row = D(taskLists[position].name, 0, 0) //TODO tasks, overdue tasks text

        viewHolder.nameTextView.text = row.name

        val tasksText = SpannableStringBuilder()
        if(row.tasksCount > 0) {
            tasksText.color(ContextCompat.getColor(context, R.color.alert_blue))
            { append(context.getString(R.string.task_lists_item_tasks_count, row.tasksCount)) }

            if(row.overdueTasksCount > 0) {
                tasksText.append(" ")
                tasksText.color(ContextCompat.getColor(context, R.color.alert_red))
                { append(context.getString(R.string.task_lists_item_overdue_tasks_count, row.overdueTasksCount)) }
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

    fun update(newTaskLists: List<TaskList>) {
        taskLists.clear()
        taskLists.addAll(newTaskLists)

        notifyDataSetChanged()
    }
}