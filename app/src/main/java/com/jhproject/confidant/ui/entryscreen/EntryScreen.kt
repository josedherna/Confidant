package com.jhproject.confidant.ui.entryscreen

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun EntryScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

    }
}

@Preview
@Composable
fun EntryFAB() {
    FloatingActionButton(onClick = { /*TODO*/ },
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal))) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
    }
}

@Preview
@Composable
fun MonthLabel() {
    Text("",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold)
}

@Composable
fun EntryCard() {

}