package ru.dabudetsvet.develop.harmonization.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.dabudetsvet.develop.harmonization.ui.goals.GoalsScreen
import ru.dabudetsvet.develop.harmonization.ui.history.HistoryScreen
import ru.dabudetsvet.develop.harmonization.ui.wheel.WheelScreen

private sealed class Destination(val route: String, val label: String, val icon: ImageVector) {
    data object Wheel : Destination("wheel", "Колесо", Icons.Filled.DonutLarge)
    data object Goals : Destination("goals", "Цели", Icons.Filled.EmojiEvents)
    data object History : Destination("history", "История", Icons.Filled.History)
}

private val destinations = listOf(Destination.Wheel, Destination.Goals, Destination.History)

@Composable
fun HarmonizationNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            NavigationBar {
                destinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Wheel.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Wheel.route) { WheelScreen() }
            composable(Destination.Goals.route) { GoalsScreen() }
            composable(Destination.History.route) { HistoryScreen() }
        }
    }
}
