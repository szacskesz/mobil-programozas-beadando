package hu.szacskesz.mobile.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.settings.SettingsActivity
import hu.szacskesz.mobile.tasklist.tasklists.TaskListsActivity


class MainActivity : BaseLanguageAwareActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_task_lists -> {
                this.startActivity(Intent(this, TaskListsActivity::class.java))
            }
            R.id.menu_action_settings -> {
                this.startActivity(Intent(this, SettingsActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
