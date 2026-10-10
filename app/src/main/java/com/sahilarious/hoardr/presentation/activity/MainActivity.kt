package com.sahilarious.hoardr.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
            HoardrTheme {
                AppNavGraph(onboardingManager = onboardingManager)
            }
        }
    }
}
