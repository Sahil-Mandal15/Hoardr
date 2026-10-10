package com.sahilarious.hoardr.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

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

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
    }
}
