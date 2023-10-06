package evgeniy.ryzhikov.filmsearch.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode

class LowBatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (isNightThemeUsed == null) isNightThemeUsed = checkUsedNightTheme(context)

        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> changeTheme(AppCompatDelegate.MODE_NIGHT_YES)
            Intent.ACTION_POWER_CONNECTED -> changeTheme(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun checkUsedNightTheme(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    private fun changeTheme(@NightMode mode: Int) {
        if (!isNightThemeUsed!!) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    companion object {
        //Проверка, не используется ли темная тема по умолчанию
        private var isNightThemeUsed: Boolean? = null
    }
}