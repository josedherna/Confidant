package com.jhproject.confidant.ui.mainscreen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jhproject.confidant.ui.entryscreen.EntryScreen
import com.jhproject.confidant.ui.settingscreen.SettingScreen
import com.jhproject.confidant.ui.statscreen.StatScreen
import com.jhproject.confidant.R

//Defines the primary app screens
enum class PrimaryAppScreen(val route: String, val title: Int, val icon: Int, val filledIcon: Int) {
    ENTRIES("entry_screen", R.string.nav_1, R.drawable.book_24px, R.drawable.book_filled_24px),
    STATS("stat_screen", R.string.nav_2, R.drawable.chart_data_24px, R.drawable.chart_data_filled_24px),
    SETTINGS("setting_screen", R.string.nav_3, R.drawable.settings_24px, R.drawable.settings_filled_24px)
}

//List of destinations that the navigation bar/rail will display
private val destinations = PrimaryAppScreen.entries

@Immutable
data class ScreenIcons(
    val default: ImageVector,
    val filled: ImageVector
)

@Composable
fun rememberScreenIcons(screen: PrimaryAppScreen): ScreenIcons {
    val unselectedPainter = ImageVector.vectorResource(screen.icon)
    val selectedPainter = ImageVector.vectorResource(screen.filledIcon)

    return remember(screen) {
        ScreenIcons(default = unselectedPainter, filled = selectedPainter)
    }
}

@Composable
fun NavIcon(screen: PrimaryAppScreen, selected: Boolean) {
    val icons = rememberScreenIcons(screen)

    Icon(
        imageVector = if (selected) icons.filled else icons.default,
        contentDescription = stringResource(screen.title)
    )
}

@Composable
fun AppNavRail(navController: NavController) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Spacer(Modifier.weight(1f))

        destinations.forEach { screen ->
            AppNavRailItem(
                screen = screen,
                navController = navController
            )
        }

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun AppNavRailItem(
    screen: PrimaryAppScreen,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isSelected = screen.route == currentRoute

    NavigationRailItem(
        selected = isSelected,
        onClick = {
            if (!isSelected) {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        icon = { NavIcon(screen, isSelected) },
        label = { Text(stringResource(screen.title)) }
    )
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass,
    navigateToSearch: () -> Unit,
    openEntryCreationSheet: () -> Unit,
    darkTheme: Boolean,
    mainScreenViewModel: MainScreenViewModel
) {
    val navbarController = rememberNavController()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact || isLandscape


    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (useNavRail) {
            AppNavRail(navController = navbarController)
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0),
            topBar = {
                DateTopBar(
                    dateViewModel = mainScreenViewModel,
                    searchClicked = navigateToSearch,
                    darkTheme = darkTheme
                )
            },
            floatingActionButton = {
                EntryFab(openEntryCreationSheet)
            },
            bottomBar = {
                if (!useNavRail) {
                    NavigationBar {
                        val navBackStackEntry by navbarController.currentBackStackEntryAsState()
                        val currentRoute =
                            navBackStackEntry?.destination?.route ?: destinations.first().route
                        val selectedDestinationIndex =
                            destinations.indexOfFirst { it.route == currentRoute }

                        destinations.forEachIndexed { index, screen ->
                            val isSelected = selectedDestinationIndex == index

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) navbarController.navigate(screen.route) {
                                        popUpTo(navbarController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    NavIcon(
                                        screen,
                                        isSelected
                                    )
                                },
                                label = { Text(stringResource(screen.title)) }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->

            NavHost(
                navController = navbarController,
                startDestination = PrimaryAppScreen.ENTRIES.route,
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                composable(PrimaryAppScreen.ENTRIES.route) {
                    EntryScreen(mainScreenViewModel = mainScreenViewModel, darkTheme = darkTheme)
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

@Composable
fun MonthLabel(date: String) {
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
fun MonthIconButton(
    scrollMonths: () -> Unit,
    enabled: Boolean,
    icon: ImageVector
) {
    OutlinedIconButton(
        onClick = scrollMonths,
        enabled = enabled,
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
fun SearchButton(navigateToSearch: () -> Unit) {
    FilledIconButton(
        onClick = navigateToSearch,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTopBar(
    dateViewModel: MainScreenViewModel,
    darkTheme: Boolean,
    searchClicked: () -> Unit = { }
) {
    val backgroundTheme = if (darkTheme) {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }
    else {
        MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
    }

    val months by dateViewModel.availableMonths.collectAsState(emptyList())
    val selectedMonth by dateViewModel.initSelectedMonth.collectAsState()

    //Auto-selects month if null
    LaunchedEffect(months) {
        dateViewModel.initSelectedMonth(months)
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
                MonthIconButton(
                    scrollMonths = {
                        val index = months.indexOf(selectedMonth)
                        if (index < months.lastIndex) dateViewModel.setSelectedMonth(months[index + 1])
                    },
                    enabled = months.indexOf(selectedMonth) < months.lastIndex,
                    icon = ImageVector.vectorResource(R.drawable.arrow_back_24px))
                MonthLabel(selectedMonth?.label() ?: "")
                MonthIconButton(
                    scrollMonths = {
                        val index = months.indexOf(selectedMonth)
                        if (index > 0) dateViewModel.setSelectedMonth(months[index - 1])
                    },
                    enabled = months.indexOf(selectedMonth) > 0,
                    icon = ImageVector.vectorResource(R.drawable.arrow_forward_24px),
                )
            }
        },
        actions = {
            SearchButton(searchClicked)
        }
    )
}

@Composable
fun EntryFab(
    openEntryCreationSheet: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = openEntryCreationSheet,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.add_24px),
                contentDescription = stringResource(R.string.create_description))
        },
        text = {
            Text(text = stringResource(R.string.new_entry))
        },
        modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal
                )
            )
    )
}
