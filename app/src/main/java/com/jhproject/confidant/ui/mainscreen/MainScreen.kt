package com.jhproject.confidant.ui.mainscreen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jhproject.confidant.ui.entryscreen.EntryScreen
import com.jhproject.confidant.ui.settingscreen.SettingScreen
import com.jhproject.confidant.ui.statscreen.StatScreen
import com.jhproject.confidant.R

enum class PrimaryAppScreen(val route: String, val title: Int, val icon: Int, val filledIcon: Int) {
    ENTRIES("entry_screen", R.string.nav_1, R.drawable.book_24px, R.drawable.book_filled_24px),
    STATS("stat_screen", R.string.nav_2, R.drawable.chart_data_24px, R.drawable.chart_data_filled_24px),
    SETTINGS("setting_screen", R.string.nav_3, R.drawable.settings_24px, R.drawable.settings_filled_24px)
}

enum class FabActions(val icon: Int, val label: Int) {
    ADD_TODAY_ENTRY(R.drawable.edit_24px, R.string.add_today),
    ADD_OTHER_ENTRY(R.drawable.otherday_24px, R.string.add_other),
    ADD_IMPORTANT_DAY(R.drawable.stars_24px, R.string.add_important)
}

private val destinations = PrimaryAppScreen.entries

@Composable
fun NavIcon(
    isSelected: Boolean,
    screen: PrimaryAppScreen
) {
    val iconId = if (isSelected) screen.filledIcon else screen.icon
    val contentDescription = stringResource(screen.title)

    Icon(
        imageVector = ImageVector.vectorResource(iconId),
        contentDescription = contentDescription
    )
}

@Composable
fun NavLabel(screen: PrimaryAppScreen) {
    Text(stringResource(screen.title))
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(windowSizeClass: WindowSizeClass) {
    val navbarController = rememberNavController()

    val navBackStackEntry by navbarController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: destinations.first().route

    val selectedDestinationIndex = destinations.indexOfFirst { it.route == currentRoute }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //Shows navigation rail on tablets and when device is in landscape
    if (windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact || isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Spacer(modifier = Modifier.weight(1f))

                destinations.forEachIndexed { index, screen ->
                    val isSelected = selectedDestinationIndex == index

                    NavigationRailItem(
                        //Use the derived index for 'selected'
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navbarController.navigate(screen.route)
                                {
                                    popUpTo(navbarController.graph.startDestinationId) {
                                        //Saves state of the previous screen
                                        saveState = true
                                    }
                                    //Avoids creating multiple copies of the same destination on the stack
                                    launchSingleTop = true
                                    //Restores state when re-selecting a previously selected item
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            NavIcon(isSelected, screen)
                        },
                        label = {
                            NavLabel(screen)
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0),
                topBar = {
                    DateTopBar()
                },
                floatingActionButton = {
                    EntryFAB()
                }
            ) { paddingValues ->
                NavHost(
                    navController = navbarController,
                    startDestination = PrimaryAppScreen.ENTRIES.route,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
    }
    else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0),
            topBar = {
                DateTopBar()
            },
            floatingActionButton = {
                EntryFAB()
            },
            bottomBar = {
                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                    NavigationBar(
                        windowInsets = NavigationBarDefaults.windowInsets,
                    ) {
                        destinations.forEachIndexed { index, screen ->
                            val isSelected = selectedDestinationIndex == index

                            NavigationBarItem(
                                //Use the derived index for 'selected'
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        navbarController.navigate(screen.route)
                                        {
                                            popUpTo(navbarController.graph.startDestinationId) {
                                                //Saves state of the previous screen
                                                saveState = true
                                            }
                                            //Avoids creating multiple copies of the same destination on the stack
                                            launchSingleTop = true
                                            //Restores state when re-selecting a previously selected item
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    NavIcon(isSelected, screen)
                                },
                                label = {
                                    NavLabel(screen)
                                }
                            )
                        }
                    }
                }
            },
        ) { paddingValues ->
            NavHost(
                navController = navbarController,
                startDestination = PrimaryAppScreen.ENTRIES.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
            imageVector = icon, contentDescription = stringResource(R.string.month_seek)
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
            imageVector = ImageVector.vectorResource(R.drawable.search_24px),
            contentDescription = stringResource(R.string.search_desc)
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundTheme
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                MonthIconButton(ImageVector.vectorResource(R.drawable.arrow_back_24px))
                MonthLabel(dateViewModel.selectedDate)
                MonthIconButton(ImageVector.vectorResource(R.drawable.arrow_forward_24px))
            }
        },
        actions = {
            SearchButton()
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EntryFAB() {
    var fabExpanded by rememberSaveable { mutableStateOf(false) }
    val containerColor = if (fabExpanded) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryContainer

    FloatingActionButtonMenu(
        expanded = fabExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = fabExpanded,
                onCheckedChange = {
                    fabExpanded = it
                },
                containerColor = { containerColor },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (fabExpanded) R.drawable.close_24px else R.drawable.add_24px),
                    contentDescription = stringResource(R.string.create_fab),
                    tint = if (fabExpanded) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        },
        Modifier
            .offset(x = ((16).dp), y = ((16).dp))
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal
                )
            )
    ) {
        FabActions.entries.forEach { actions ->
            FloatingActionButtonMenuItem(
                onClick = {
                    fabExpanded = false
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(actions.icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                },
                text = {
                    Text(
                        text = stringResource(actions.label),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            )
        }
    }
}