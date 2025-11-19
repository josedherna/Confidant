package com.jhproject.confidant.ui.entryscreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview
@Composable
fun EntryScreen(
    entryViewModel: EntryScreenViewModel = viewModel(),
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val backgroundTheme = if (darkTheme) {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }
    else {
        MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
    }

    Surface(
        color = backgroundTheme,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
        ) {

        }
    }
}

@Composable
fun EntryCard() {

}