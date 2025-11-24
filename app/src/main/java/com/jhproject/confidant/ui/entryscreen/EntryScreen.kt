package com.jhproject.confidant.ui.entryscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhproject.confidant.R

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
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 20.dp
            )
        ) {
            item {
                EntryCard()
            }
            item {
                EntryCard(mood = "good")
            }
            item {
                EntryCard()
            }
            item {
                EntryCard(mood = "meh")
            }
            item {
                EntryCard(mood = "good")
            }
        }
    }
}

data class MoodResources(
    val icon: Int,
    val label: String
)

@Preview
@Composable
fun EntryCard(
    date: String = "Tuesday, November 18",
    time: String = "10:00 PM",
    mood: String = "great",
    note: String = "I had a great day today. We got to see the show.",
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val backgroundTheme = if (darkTheme) {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    else {
        MaterialTheme.colorScheme.surface
    }

    val entryMood = when (mood) {
        "great" -> MoodResources(R.drawable.sentiment_excited_24px, stringResource(R.string.great))
        "good" -> MoodResources(R.drawable.sentiment_satisfied_24px, stringResource(R.string.good))
        "meh" -> MoodResources(R.drawable.sentiment_neutral_24px, stringResource(R.string.meh))
        "bad" -> MoodResources(R.drawable.sentiment_dissatisfied_24px, stringResource(R.string.bad))
        "awful" -> MoodResources(R.drawable.sentiment_sad_24px, stringResource(R.string.awful))
        else -> {
            MoodResources(R.drawable.sentiment_neutral_24px, stringResource(R.string.meh))
        }
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = backgroundTheme
        ),
        modifier = Modifier
            .widthIn(max = 600.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = painterResource(entryMood.icon),
                        contentDescription = "Mood",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row() {
                        Column() {
                            Text(
                                text = date,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ).toSpanStyle(
                                        ),
                                    ) {
                                        append("${entryMood.label} ")
                                    }

                                    withStyle(
                                        style = MaterialTheme.typography.titleMedium.toSpanStyle()
                                    ) {
                                        append("$time ")
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        EntrySettingsButton()
                    }
                    Text(
                        text = note,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EntrySettingsButton() {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedIconButton(
            onClick = {
                expanded = !expanded
            },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = IconButtonDefaults.outlinedIconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                ImageVector.vectorResource(
                R.drawable.more_horiz_24px
                ),
                contentDescription = stringResource(R.string.edit_description)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp)
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(R.drawable.edit_24px),
                        contentDescription = null
                    )
                },
                text = {
                    Text(stringResource(R.string.edit))
                },
                onClick = {
                    expanded = false
                },
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(R.drawable.delete_24px),
                        contentDescription = null
                    )
                },
                text = {
                    Text(stringResource(R.string.delete))
                },
                onClick = {
                    expanded = false
                }
            )
        }
    }
}