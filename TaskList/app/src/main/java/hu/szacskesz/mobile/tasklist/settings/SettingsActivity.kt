package hu.szacskesz.mobile.tasklist.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import com.takisoft.preferencex.PreferenceFragmentCompat
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.core.domain.TaskList
import hu.szacskesz.mobile.tasklist.utils.Constants
import java.util.*


class SettingsActivity : BaseLanguageAwareActivity() {

    private lateinit var taskLists: List<TaskList>

    override fun onCreate(savedInstanceState: Bundle?) {
        // Solves UninitialisedPropertyAccess in fragment after activity recreate
        taskLists = intent.getParcelableArrayListExtra(Constants.IntentExtra.Key.TASK_LISTS)!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String? ) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val taskLists = (activity as SettingsActivity?)?.taskLists ?: listOf()

            val languagePref: ListPreference? = findPreference(getString(R.string.settings_language_key))
            languagePref?.setOnPreferenceChangeListener { _, _ ->
                activity?.recreate()
                return@setOnPreferenceChangeListener true
            }

            val defaultTaskListListPreference: ListPreference? = findPreference(getString(R.string.settings_default_task_list_key))
            defaultTaskListListPreference?.entries =
                mutableListOf<String>().apply {
                    add(getString(Constants.TaskList.ALL.name))
                    addAll(taskLists.map{ it.name })
                }.toTypedArray()
            defaultTaskListListPreference?.entryValues =
                mutableListOf<String>().apply {
                    add(Constants.TaskList.ALL.id.toString())
                    addAll(taskLists.map{ it.id.toString() })
                }.toTypedArray()
            defaultTaskListListPreference?.value = sharedPref.getString(
                getString(R.string.settings_default_task_list_key),
                Constants.TaskList.ALL.id.toString()
            )
        }
    }
}
