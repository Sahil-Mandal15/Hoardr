package com.sahilarious.hoardr.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class OnboardingManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("hoardr_prefs", Context.MODE_PRIVATE)

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) {
            prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, value).apply()
        }

    var themeMode: String
        get() = prefs.getString(KEY_THEME_MODE, THEME_SYSTEM) ?: THEME_SYSTEM
        set(value) {
            prefs.edit().putString(KEY_THEME_MODE, value).apply()
        }

    fun shouldAutoOpenExplore(): Boolean {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString(KEY_LAST_OPEN_DATE, "") ?: ""
        
        var count = prefs.getInt(KEY_DAILY_TRIGGER_COUNT, 0)
        var wasLastExplore = prefs.getBoolean(KEY_WAS_LAST_OPEN_EXPLORE, false)

        if (lastDate != today) {
            prefs.edit()
                .putString(KEY_LAST_OPEN_DATE, today)
                .putInt(KEY_DAILY_TRIGGER_COUNT, 0)
                .putBoolean(KEY_WAS_LAST_OPEN_EXPLORE, false)
                .apply()
            count = 0
            wasLastExplore = false
        }

        if (count >= 2 || wasLastExplore) {
            prefs.edit().putBoolean(KEY_WAS_LAST_OPEN_EXPLORE, false).apply()
            return false
        }

        val shouldOpen = Random.nextFloat() < 0.45f
        if (shouldOpen) {
            prefs.edit()
                .putInt(KEY_DAILY_TRIGGER_COUNT, count + 1)
                .putBoolean(KEY_WAS_LAST_OPEN_EXPLORE, true)
                .apply()
        } else {
            prefs.edit().putBoolean(KEY_WAS_LAST_OPEN_EXPLORE, false).apply()
        }

        return shouldOpen
    }

    fun themeModeFlow(): Flow<String> = callbackFlow {
        trySend(themeMode)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_THEME_MODE) {
                trySend(themeMode)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_LAST_OPEN_DATE = "key_last_open_date"
        private const val KEY_DAILY_TRIGGER_COUNT = "key_daily_trigger_count"
        private const val KEY_WAS_LAST_OPEN_EXPLORE = "key_was_last_open_explore"
        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }
}
