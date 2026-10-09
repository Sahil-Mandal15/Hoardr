package com.sahilarious.hoardr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sahilarious.hoardr.presentation.screens.HomeScreen
import com.sahilarious.hoardr.presentation.screens.HoardrWelcomeScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Onboarding.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.Onboarding.route) {
            HoardrWelcomeScreen {
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
