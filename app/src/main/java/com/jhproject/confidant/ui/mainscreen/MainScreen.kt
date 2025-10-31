package com.jhproject.confidant.ui.mainscreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jhproject.confidant.ui.entryscreen.EntryScreen
import com.jhproject.confidant.ui.settingscreen.SettingScreen
import com.jhproject.confidant.ui.statscreen.StatScreen
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.entryscreen.EntryFAB

@Preview
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            if (currentRoute == PrimaryAppScreen.ENTRIES.route) {
                EntryFAB()
            }
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = PrimaryAppScreen.ENTRIES.route,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            composable(PrimaryAppScreen.ENTRIES.route) {
                EntryScreen()
            }
            composable(PrimaryAppScreen.STATS.route) {
                StatScreen()
            }
            composable(PrimaryAppScreen.SETTINGS.route) {
                SettingScreen()
            }
        }
    }
}

enum class PrimaryAppScreen(val route: String, val title: String, val icon: Int, val filledIcon: Int) {
    ENTRIES("entry_screen", "Entries", R.drawable.book_24px, R.drawable.book_filled_24px),
    STATS("stat_screen", "Stats", R.drawable.chart_data_24px, R.drawable.chart_data_filled_24px),
    SETTINGS("setting_screen", "Settings", R.drawable.settings_24px, R.drawable.settings_filled_24px)
}

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        PrimaryAppScreen.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.endsWith(item.route) == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                },
                icon = {
                    Icon(
                        painter = if (currentRoute == item.route) painterResource(item.filledIcon) else painterResource(item.icon),
                        contentDescription = item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}