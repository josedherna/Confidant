package com.jhproject.confidant.ui.mainscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        topBar = {
            DateTopBar()
        },
        floatingActionButton = {
            EntryFAB()
        },
        bottomBar = {
            BottomNavBar(navController)
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = PrimaryAppScreen.ENTRIES.route,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues),
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

enum class PrimaryAppScreen(val route: String, val title: Int, val icon: Int, val filledIcon: Int) {
    ENTRIES("entry_screen", R.string.nav_1, R.drawable.book_24px, R.drawable.book_filled_24px),
    STATS("stat_screen", R.string.nav_2, R.drawable.chart_data_24px, R.drawable.chart_data_filled_24px),
    SETTINGS("setting_screen", R.string.nav_3, R.drawable.settings_24px, R.drawable.settings_filled_24px)
}

@Preview
@Composable
fun MonthLabel(date: String = "October 2025") {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable {

            }
            .padding(15.dp)
    ) {
        Text(
            text = date,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun MonthIconButton(icon: ImageVector) {
    OutlinedIconButton(
        onClick = { },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = Modifier.size(32.dp),
    ) {
        Icon(
            imageVector = icon, contentDescription = "Scroll"
        )
    }
}

@Composable
fun SearchButton() {
    FilledIconButton(
        onClick = { },
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal
                )
        )
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "Search"
        )
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTopBar(
    dateViewModel: MainScreenViewModel = viewModel(),
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val backgroundTheme = if (darkTheme) {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }
    else {
        MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
    }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundTheme
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                MonthIconButton(Icons.AutoMirrored.Filled.ArrowBack)
                MonthLabel(dateViewModel.selectedDate)
                MonthIconButton(Icons.AutoMirrored.Filled.ArrowForward)
            }
        },
        actions = {
            SearchButton()
        }
    )
}

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        PrimaryAppScreen.entries.forEach { item ->
            LocalContext.current
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
                        contentDescription = stringResource(item.title))
                },
                label = {
                    Text(stringResource(item.title))
                },
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.navigationBars
                    )
            )
        }
    }
}