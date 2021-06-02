package hu.szacskesz.mobile.tasklist.common

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import androidx.core.app.JobIntentService
import androidx.preference.PreferenceManager
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.utils.ContextUtils
import java.util.*


abstract class BaseLanguageAwareJonIntentService : JobIntentService() {
    override fun attachBaseContext(context: Context) {
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val languageCode = sharedPref.getString(
            context.getString(R.string.settings_language_key),
            context.getString(R.string.settings_language_values_default)
        )!!
        val locale = Locale(languageCode)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(context, locale)

        super.attachBaseContext(localeUpdatedContext)
    }
}
