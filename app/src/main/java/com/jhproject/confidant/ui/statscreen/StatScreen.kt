package com.jhproject.confidant.ui.statscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.entryscreen.Mood
import com.jhproject.confidant.ui.mainscreen.MainScreenViewModel

@Composable
fun StatScreen(
    darkTheme: Boolean,
    viewModel: MainScreenViewModel,
) {
    val background = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
    val listState = rememberLazyListState()

    Surface(
        color = background,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 88.dp
            ),
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal)
                )
        ) {
            item {
                CommonMoodCard(
                    darkTheme = darkTheme,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun CommonMoodCard(
    darkTheme: Boolean,
    viewModel: MainScreenViewModel
) {
    val mostCommonMood by viewModel.mostCommonMood.collectAsState(null)
    val mood = Mood.fromKey(mostCommonMood ?: "meh")

    val titleStyle = MaterialTheme.typography.headlineSmall
    val moodLabelStyle = MaterialTheme.typography.headlineLarge

    val label = if (mostCommonMood == null ) stringResource(R.string.not_available) else stringResource(mood.label)
    val icon = rememberVectorPainter(ImageVector.vectorResource(mood.icon))
    val color = if (darkTheme) mood.darkColor else mood.lightColor

    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.widthIn(max = 600.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.common_mood_label),
                style = titleStyle,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = icon,
                    tint = color,
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = label,
                    color = color,
                    style = moodLabelStyle,
                    fontWeight = FontWeight.Bold
                )
            }
            if (mostCommonMood == null) {
                Text(
                    text = stringResource(R.string.stats_empty)
                )
            }
        }
    }
}

@Composable
fun MoodCountCard() {
    OutlinedCard(

    ) {

    }
}

@Composable
fun LifetimeEntryCount() {

}