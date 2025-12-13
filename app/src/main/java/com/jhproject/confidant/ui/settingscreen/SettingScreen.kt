package com.jhproject.confidant.ui.settingscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.mainscreen.MainScreenViewModel

@Composable
fun SettingScreen(
    darkTheme: Boolean,
    viewModel: MainScreenViewModel
) {
    val background = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
    var showDeleteMonthConfirmationDialog by remember { mutableStateOf(false) }
    var showDeleteAllConfirmationDialog by remember { mutableStateOf(false) }


    Surface(
        color = background,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 88.dp
            ),
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    )
                )
        ) {
            item {
                ListItem(
                    leadingContent =  {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.delete_24px),
                            contentDescription = null
                        )
                    },
                    headlineContent = { Text(text = stringResource(R.string.deletemonth_itemtitle)) },
                    supportingContent = { Text(text = stringResource(R.string.deletemonth_itemtitle)) },
                    colors = ListItemDefaults.colors(
                        containerColor = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .clickable {
                            showDeleteMonthConfirmationDialog = true
                        }
                )
            }
            item {
                ListItem(
                    leadingContent =  {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.warning_24px),
                            contentDescription = null
                        )
                    },
                    headlineContent = { Text(text = stringResource(R.string.deleteall_itemtitle)) },
                    supportingContent = { Text(text = stringResource(R.string.deleteall_itemsub)) },
                    colors = ListItemDefaults.colors(
                        containerColor = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .clickable {
                            showDeleteAllConfirmationDialog = true
                        }
                )
            }
        }

        DeleteMonthEntriesDialog(
            isVisible = showDeleteMonthConfirmationDialog,
            onConfirm = {
                showDeleteMonthConfirmationDialog = false
                viewModel.deleteCurrentMonthEntries()
            },
            onDismiss = {
                showDeleteMonthConfirmationDialog = false
            }
        )

        DeleteAllEntriesDialog(
            isVisible = showDeleteAllConfirmationDialog,
            onConfirm = {
                showDeleteAllConfirmationDialog = false
                viewModel.deleteAllJournalEntries()
            },
            onDismiss = {
                showDeleteAllConfirmationDialog = false
            }
        )
    }
}

@Composable
fun DeleteMonthEntriesDialog(
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogIcon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.delete_24px))

    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    painter = dialogIcon, contentDescription = null
                )
            },
            title = {
                Text(stringResource(R.string.deletemonth_title))
            },
            text = {
                Text(stringResource(R.string.deletemonth_description))
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = stringResource(R.string.delete_action))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel_label))
                }
            },
        )
    }
}

@Composable
fun DeleteAllEntriesDialog(
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogIcon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.delete_24px))

    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    painter = dialogIcon, contentDescription = null
                )
            },
            title = {
                Text(stringResource(R.string.deleteall_title))
            },
            text = {
                Text(stringResource(R.string.deleteall_description))
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = stringResource(R.string.delete_action))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel_label))
                }
            },
        )
    }
}