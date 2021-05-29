package hu.szacskesz.mobile.tasklist.tasks

import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.core.domain.TaskWithTaskListName
import kotlinx.android.synthetic.main.tasks_item.view.*
import java.util.*


class TasksAdapter (
    private val tasks: MutableList<TaskWithTaskListName> = mutableListOf(),
    private val onRowClicked: (TaskWithTaskListName) -> Unit,
    private val onCheckboxClicked: (TaskWithTaskListName) -> Unit,
) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val doneCheckbox: CheckBox = view.tasks_item_done
        val nameTextView: TextView = view.tasks_item_name
        val dueTextView: TextView = view.tasks_item_due
        val listNameTextView: TextView = view.tasks_item_list_name
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.tasks_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        val task = tasks[position]

        viewHolder.doneCheckbox.isChecked = task.done
        viewHolder.nameTextView.text = task.description

        if(task.deadline != null) {
            val color = if(Date().before(task.deadline!!))
                ContextCompat.getColor(context, R.color.alert_blue)
            else
                ContextCompat.getColor(context, R.color.alert_red)

            viewHolder.dueTextView.text = SpannableStringBuilder().color(color) {
                append(DateFormat.getDateFormat(context).format(task.deadline!!))
                append(" ")
                append(DateFormat.getTimeFormat(context).format(task.deadline!!))
            }
            viewHolder.dueTextView.visibility = View.VISIBLE
        } else {
            viewHolder.dueTextView.text = ""
            viewHolder.dueTextView.visibility = View.GONE
        }

        if(task.listName != null) {
            viewHolder.listNameTextView.text = task.listName!!
            viewHolder.listNameTextView.visibility = View.VISIBLE
        } else {
            viewHolder.listNameTextView.text = ""
            viewHolder.listNameTextView.visibility = View.GONE
        }

        //listeners
        viewHolder.itemView.setOnClickListener {
            onRowClicked(task)
        }
        viewHolder.itemView.tasks_item_done.setOnClickListener {
            onCheckboxClicked(task)
        }
    }

    override fun getItemCount() = tasks.size

    fun update(newTasks: List<TaskWithTaskListName>) {
        tasks.clear()
        tasks.addAll(newTasks)

        notifyDataSetChanged()
    }
}