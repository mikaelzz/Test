package com.example.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.viewmodel.HabitViewModel

@Composable
fun MainScreen(viewModel: HabitViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.DASHBOARD } == true,
                    onClick = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
                    label = { Text("Calendar") },
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.CALENDAR } == true,
                    onClick = {
                        navController.navigate(Routes.CALENDAR) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Achievements") },
                    label = { Text("Badges") },
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.ACHIEVEMENTS } == true,
                    onClick = {
                        navController.navigate(Routes.ACHIEVEMENTS) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.DASHBOARD) {
                DashboardScreen(viewModel, onAddHabitClick = { navController.navigate(Routes.ADD_HABIT) })
            }
            composable(Routes.CALENDAR) {
                CalendarScreen(viewModel)
            }
            composable(Routes.ACHIEVEMENTS) {
                AchievementsScreen(viewModel)
            }
            composable(Routes.ADD_HABIT) {
                AddHabitScreen(
                    onSave = { name, desc, freq, sound ->
                        viewModel.addHabit(name, desc, freq, sound)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}
