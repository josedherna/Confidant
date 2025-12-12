package com.jhproject.confidant.ui.statscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.jhproject.confidant.data.MoodCount
import com.jhproject.confidant.ui.entryscreen.Mood
import com.jhproject.confidant.ui.mainscreen.MainScreenViewModel

@Composable
fun StatScreen(
    darkTheme: Boolean,
    viewModel: MainScreenViewModel,
) {
    val background = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
    val listState = rememberLazyListState()

    val mostCommonMood by viewModel.mostCommonMood.collectAsState(null)
    val monthlyEntryCount by viewModel.monthlyEntryCount.collectAsState(null)
    val lifetimeEntryCount by viewModel.lifetimeEntryCount.collectAsState(null)

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
                    mostCommonMood = mostCommonMood
                )
            }
            item {
                MoodCountsCard(
                    darkTheme = darkTheme,
                    viewModel = viewModel
                )
            }
            item {
                MonthlyEntryCount(
                    darkTheme = darkTheme,
                    monthlyEntryCount = monthlyEntryCount
                )
            }
            item {
                LifetimeEntryCount(
                    darkTheme = darkTheme,
                    lifetimeEntryCount = lifetimeEntryCount
                )
            }
        }
    }
}

@Composable
fun CommonMoodCard(
    darkTheme: Boolean,
    mostCommonMood: String?
) {
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
            horizontalAlignment = Alignment.Start,
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
fun MoodPieChart(
    counts: List<MoodCount>,
    darkTheme: Boolean
) {
    if (counts.isEmpty()) {
        Text("No data available.")
        return
    }

    val total = counts.sumOf { it.count }

    Canvas(Modifier.size(180.dp)) {
        var startAngle = -90f

        counts.forEach { item ->
            val sweep = 360f * (item.count / total.toFloat())
            drawArc(
                color = if (darkTheme) Mood.fromKey(item.mood).darkColor else Mood.fromKey(item.mood).lightColor,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true
            )
            startAngle += sweep
        }
    }
}

@Composable
fun EmptyMoodCountsCard(darkTheme: Boolean) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (darkTheme)
                MaterialTheme.colorScheme.surfaceContainerHigh
            else
                MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "Mood Counts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("No entries for this month.")
        }
    }
}

@Composable
fun MoodCountsCard(
    darkTheme: Boolean,
    viewModel: MainScreenViewModel
) {
    val moodCounts by viewModel.moodCounts.collectAsState()

    if (moodCounts.isEmpty()) {
        EmptyMoodCountsCard(darkTheme)
        return
    }

    val cardColor = if (darkTheme)
        MaterialTheme.colorScheme.surfaceContainerHigh
    else
        MaterialTheme.colorScheme.surface

    val titleStyle = MaterialTheme.typography.headlineSmall
    val moodLabelStyle = MaterialTheme.typography.headlineSmall

    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = cardColor),
        modifier = Modifier.widthIn(max = 600.dp)
    ) {
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Mood Counts",
                style = titleStyle,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                MoodPieChart(
                    counts = moodCounts,
                    darkTheme = darkTheme
                )
            }

            // List each mood with count
            moodCounts.forEach { item ->
                val mood = Mood.fromKey(item.mood)

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = rememberVectorPainter(ImageVector.vectorResource(mood.icon)),
                            tint = if (darkTheme) mood.darkColor else mood.lightColor,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = stringResource(mood.label),
                            style = moodLabelStyle,
                            fontWeight = FontWeight.Bold,
                            color = if (darkTheme) mood.darkColor else mood.lightColor
                        )
                    }

                    Text(
                        text = item.count.toString(),
                        style = titleStyle,
                        fontWeight = FontWeight.Bold,
                        color = if (darkTheme) mood.darkColor else mood.lightColor
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyEntryCount(
    darkTheme: Boolean,
    monthlyEntryCount: Int?
) {
    val titleStyle = MaterialTheme.typography.headlineSmall
    val entryLabelStyle = MaterialTheme.typography.headlineLarge
    val entryLabel = if (monthlyEntryCount != 1) stringResource(R.string.entries_label) else stringResource(R.string.one_entry_label)


    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .widthIn(max = 600.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.monthly_label),
                style = titleStyle,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = monthlyEntryCount.toString(),
                    style = entryLabelStyle,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = entryLabel,
                    style = entryLabelStyle,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LifetimeEntryCount(
    darkTheme: Boolean,
    lifetimeEntryCount: Int?
) {
    val titleStyle = MaterialTheme.typography.headlineSmall
    val entryLabelStyle = MaterialTheme.typography.headlineLarge
    val entryLabel = if (lifetimeEntryCount != 1) stringResource(R.string.entries_label) else stringResource(R.string.one_entry_label)

    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .widthIn(max = 600.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.lifetime_label),
                style = titleStyle,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = lifetimeEntryCount.toString(),
                    style = entryLabelStyle,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = entryLabel,
                    style = entryLabelStyle,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}