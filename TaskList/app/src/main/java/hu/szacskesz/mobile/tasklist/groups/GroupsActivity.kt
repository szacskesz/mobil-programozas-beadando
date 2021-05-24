package hu.szacskesz.mobile.tasklist.groups

import android.os.Bundle
import android.util.Log
import hu.szacskesz.mobile.tasklist.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.R
import kotlinx.android.synthetic.main.groups_activity.*


class GroupsActivity : BaseLanguageAwareActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.groups_activity)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        groups_item_add_button.setOnClickListener {
            //TODO on group add
            Log.w("TODO", "Add");
        }

        groups_recycler_view.adapter  = GroupsAdapter(/*TODO data*/)
    }

}
