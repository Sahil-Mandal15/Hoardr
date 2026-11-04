package com.sahilarious.hoardr.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sahilarious.hoardr.data.preferences.OnboardingManager
import com.sahilarious.hoardr.presentation.navigation.AppNavGraph
import com.sahilarious.hoardr.presentation.ui.theme.HoardrTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingManager: OnboardingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by onboardingManager.themeModeFlow().collectAsState(initial = onboardingManager.themeMode)
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                OnboardingManager.THEME_LIGHT -> false
                OnboardingManager.THEME_DARK -> true
                else -> systemDark
            }

            HoardrTheme(darkTheme = darkTheme) {
                AppNavGraph(onboardingManager = onboardingManager)
            }
        }
    }
}
