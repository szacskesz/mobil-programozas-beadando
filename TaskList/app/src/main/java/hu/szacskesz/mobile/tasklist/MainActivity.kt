package hu.szacskesz.mobile.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import hu.szacskesz.mobile.tasklist.groups.GroupsActivity


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
            R.id.menu_action_groups -> {
                this.startActivity(Intent(this, GroupsActivity::class.java))
            }
            R.id.menu_action_settings -> {
                this.startActivity(Intent(this, SettingsActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
