package com.jhproject.confidant.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jhproject.confidant.ui.mainscreen.MainScreen
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.searchscreen.SearchStartScreen

enum class AppScreen(val route: String) {
    MAIN_SCREEN("main_screen"),
    SEARCH_SCREEN("search_screen"),
}

@Composable
fun AppNavigationHost(
    windowSizeClass: WindowSizeClass,
    darkTheme: Boolean
) {
    val parentNavController = rememberNavController()

    NavHost(
        navController = parentNavController,
        startDestination = AppScreen.MAIN_SCREEN.route
    ) {
        composable(AppScreen.MAIN_SCREEN.route) {
            MainScreen(
                windowSizeClass,
                navigateToSearch = {
                    parentNavController.navigate(AppScreen.SEARCH_SCREEN.route)
                },
                darkTheme
            )
        }

        composable(AppScreen.SEARCH_SCREEN.route) {
            SearchStartScreen(
                darkTheme = darkTheme,
                backClicked = { parentNavController.navigateUp() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun TopBar(
    onUpClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.search_desc)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onUpClicked
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}