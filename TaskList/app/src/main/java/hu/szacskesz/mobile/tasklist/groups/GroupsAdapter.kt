package hu.szacskesz.mobile.tasklist.groups

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import hu.szacskesz.mobile.tasklist.R
import kotlinx.android.synthetic.main.groups_item.view.*


data class D (val name: String, val tasksCount: Int, val overdueTasksCount: Int)

class GroupsAdapter (
//    private val dataSet: Array<String>
//    private val documents: MutableList<Document> = mutableListOf(),
//    private val glide: RequestManager,
//    private val itemClickListener: (Document) -> Unit
) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    private lateinit var context: Context

    //TODO remove test data
    private val dataSet = arrayOf(
        D("Alfa game", 5, 2),
        D("Baba", 1, 0),
        D("Nothing", 0, 0),
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val nameTextView: TextView = view.groups_item_name
        val tasksTextView: TextView = view.groups_item_tasks

        init {
            view.setOnClickListener {
                //TODO on row click
                Log.w("TODO", "Click");
            }
            view.groups_item_edit_button.setOnClickListener {
                //TODO on edit click
                Log.w("TODO", "Edit");
            }
            view.groups_item_delete_button.setOnClickListener {
                //TODO on delete click
                Log.w("TODO", "Delete");
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context

        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.groups_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val row = dataSet[position]

        viewHolder.nameTextView.text = row.name

        val tasksText = SpannableStringBuilder()
        if(row.tasksCount > 0) {
            tasksText.color(ContextCompat.getColor(context, R.color.alert_blue))
                { append(context.getString(R.string.groups_item_tasks_count, row.tasksCount)) }

            if(row.overdueTasksCount > 0) {
                tasksText.append(" ")
                tasksText.color(ContextCompat.getColor(context, R.color.alert_red))
                    { append(context.getString(R.string.groups_item_overdue_tasks_count, row.overdueTasksCount)) }
            }
        } else {
            tasksText.color(ContextCompat.getColor(context, R.color.gray))
                { append(context.getString(R.string.groups_item_tasks_count_zero)) }
        }
        viewHolder.tasksTextView.text = tasksText
    }

    override fun getItemCount() = dataSet.size

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        glide.load(documents[position].thumbnail)
//            .error(glide.load(R.drawable.preview_missing))
//            .into(holder.previewImageView)
//        holder.previewImageView.setImageResource(R.drawable.preview_missing)
//        holder.titleTextView.text = documents[position].name
//        holder.sizeTextView.text = StringUtil.readableFileSize(documents[position].size)
//        holder.itemView.setOnClickListener { itemClickListener.invoke(documents[position]) }
//    }

//    override fun getItemCount() = documents.size

//    fun update(newDocuments: List<Document>) {
//        documents.clear()
//        documents.addAll(newDocuments)
//
//        notifyDataSetChanged()
//    }
}

//class GroupsAdapter2(private val dataSet: Array<String>) :
//    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
//
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder).
//     */
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val textView: TextView
//
//        init {
//            // Define click listener for the ViewHolder's View.
//            textView = view.findViewById(R.id.textView)
//        }
//    }
//
//    // Create new views (invoked by the layout manager)
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        // Create a new view, which defines the UI of the list item
//        val view = LayoutInflater.from(viewGroup.context)
//            .inflate(R.layout.text_row_item, viewGroup, false)
//
//        return ViewHolder(view)
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//
//        // Get element from your dataset at this position and replace the
//        // contents of the view with that element
//        viewHolder.textView.text = dataSet[position]
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//
//}
