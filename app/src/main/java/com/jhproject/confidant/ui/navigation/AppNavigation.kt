package com.jhproject.confidant.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jhproject.confidant.ui.mainscreen.MainScreen
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.entryscreen.EntryCreationBottomSheet
import com.jhproject.confidant.ui.mainscreen.MainScreenViewModel
import com.jhproject.confidant.ui.searchscreen.SearchStartScreen
import kotlinx.coroutines.launch

enum class AppScreen(val route: String) {
    MAIN_SCREEN("main_screen"),
    SEARCH_SCREEN("search_screen"),
    //ENTRY_CREATION_SCREEN("entry_creation_screen")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationHost(
    windowSizeClass: WindowSizeClass,
    mainScreenViewModel: MainScreenViewModel = viewModel(),
    darkTheme: Boolean
) {
    val parentNavController = rememberNavController()


    val sheetScope = rememberCoroutineScope()

    var showEntryCreationSheet by rememberSaveable { mutableStateOf(false) }
    val entryCreationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    NavHost(
        navController = parentNavController,
        startDestination = AppScreen.MAIN_SCREEN.route
    ) {
        composable(AppScreen.MAIN_SCREEN.route) {
            MainScreen(
                windowSizeClass = windowSizeClass,
                navigateToSearch = {
                    parentNavController.navigate(AppScreen.SEARCH_SCREEN.route)
                },
                mainScreenViewModel = mainScreenViewModel,
                openEntryCreationSheet = { showEntryCreationSheet = true },
                darkTheme = darkTheme
            )
        }

        composable(AppScreen.SEARCH_SCREEN.route) {
            SearchStartScreen(
                darkTheme = darkTheme,
                backClicked = { parentNavController.navigateUp() }
            )
        }
    }

    if (showEntryCreationSheet) {
        EntryCreationBottomSheet(
            onDismissRequest = {
                sheetScope.launch {
                    entryCreationSheetState.hide()
                    showEntryCreationSheet = false
                }.invokeOnCompletion {
                    mainScreenViewModel.setSelectedMoodCreationID(null)
                    mainScreenViewModel.onTextFieldValueChange("")
                }
            },
            sheetState = entryCreationSheetState,
            mainScreenViewModel = mainScreenViewModel,
            darkTheme = darkTheme
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class,)
@Composable
fun TopBar(
    onUpClicked: () -> Unit = {},
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title
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