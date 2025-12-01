package com.jhproject.confidant.ui.searchscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.navigation.TopBar

@Preview
@Composable
fun SearchStartScreen(
    darkTheme: Boolean = false,
    backClicked: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(backClicked)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(384.dp),
                contentAlignment = Alignment.Center
            ) {
                SearchButton()
            }
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
        }
    }
}

@Composable
fun EntrySearchBar() {

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun SearchButton() {
    val searchButtonStyle = MaterialTheme.typography.headlineSmall

    Button(onClick = { /*TODO*/ },
        modifier = Modifier
            .width(214.dp)
            .height(ButtonDefaults.LargeContainerHeight))
    {
        Text(
            text = stringResource(R.string.search_desc),
            style = searchButtonStyle

        )
    }
}