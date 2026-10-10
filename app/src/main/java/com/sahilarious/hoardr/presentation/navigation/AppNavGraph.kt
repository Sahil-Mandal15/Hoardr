package com.sahilarious.hoardr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sahilarious.hoardr.data.preferences.OnboardingManager
import com.sahilarious.hoardr.presentation.screens.HomeScreen
import com.sahilarious.hoardr.presentation.screens.HoardrWelcomeScreen

@Composable
fun AppNavGraph(
    onboardingManager: OnboardingManager,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val startDestination = if (onboardingManager.isOnboardingCompleted) {
        Screen.Home.route
    } else {
        Screen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.Onboarding.route) {
            HoardrWelcomeScreen {
                onboardingManager.isOnboardingCompleted = true
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
    }
}
