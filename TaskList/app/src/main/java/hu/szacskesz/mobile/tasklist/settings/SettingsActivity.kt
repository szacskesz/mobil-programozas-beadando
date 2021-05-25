package hu.szacskesz.mobile.tasklist.settings

import android.os.Bundle
import androidx.preference.ListPreference
import com.takisoft.preferencex.PreferenceFragmentCompat
import hu.szacskesz.mobile.tasklist.common.BaseLanguageAwareActivity
import hu.szacskesz.mobile.tasklist.R


class SettingsActivity : BaseLanguageAwareActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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

            val languagePref: ListPreference? = findPreference(getString(R.string.settings_language_key))
            languagePref?.setOnPreferenceChangeListener { preference, newValue ->
                activity?.recreate()
                return@setOnPreferenceChangeListener true
            }

            val defaultTaskListListPreference: ListPreference? = findPreference(getString(R.string.settings_default_task_list_key))
//            TODO get task-lists (names, ids), set default
            defaultTaskListListPreference?.entries = arrayOf<CharSequence>(
                getString(R.string.settings_default_task_list_default_selected_title),
                // TODO
            )
            defaultTaskListListPreference?.entryValues = arrayOf<CharSequence>(
                getString(R.string.settings_default_task_list_values_default),
                // TODO
            )
            defaultTaskListListPreference?.setDefaultValue(getString(R.string.settings_default_task_list_values_default))
        }
    }
}
