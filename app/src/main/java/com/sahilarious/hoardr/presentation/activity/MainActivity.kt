package com.sahilarious.hoardr.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sahilarious.hoardr.presentation.navigation.AppNavGraph
import com.sahilarious.hoardr.presentation.ui.theme.HoardrTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HoardrTheme {
                AppNavGraph()
            }
        }
    }
}
