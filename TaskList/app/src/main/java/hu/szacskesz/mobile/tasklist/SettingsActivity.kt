package hu.szacskesz.mobile.tasklist

import android.os.Bundle
import androidx.preference.ListPreference
import com.takisoft.preferencex.PreferenceFragmentCompat


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

            val groupListPreference: ListPreference? = findPreference(getString(R.string.settings_group_key))
//            TODO get groups (names, ids), set default
            groupListPreference?.entries = arrayOf<CharSequence>(
                getString(R.string.settings_group_default_selected_title),
                // TODO
            )
            groupListPreference?.entryValues = arrayOf<CharSequence>(
                getString(R.string.settings_group_values_default),
                // TODO
            )
            groupListPreference?.setDefaultValue(getString(R.string.settings_group_values_default))
        }
    }
}
