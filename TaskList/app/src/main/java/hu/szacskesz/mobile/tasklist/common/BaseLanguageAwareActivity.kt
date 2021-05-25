package hu.szacskesz.mobile.tasklist.common

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import hu.szacskesz.mobile.tasklist.R
import hu.szacskesz.mobile.tasklist.utils.ContextUtils
import java.util.Locale


open class BaseLanguageAwareActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
        supportActionBar?.title = getString(activityInfo.labelRes)
    }
}
