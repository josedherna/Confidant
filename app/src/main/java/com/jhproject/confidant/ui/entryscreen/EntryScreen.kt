package com.jhproject.confidant.ui.entryscreen

import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.theme.AwfulDark
import com.jhproject.confidant.ui.theme.AwfulLight
import com.jhproject.confidant.ui.theme.BadDark
import com.jhproject.confidant.ui.theme.BadLight
import com.jhproject.confidant.ui.theme.GoodDark
import com.jhproject.confidant.ui.theme.GoodLight
import com.jhproject.confidant.ui.theme.GreatDark
import com.jhproject.confidant.ui.theme.GreatLight
import com.jhproject.confidant.ui.theme.MehDark
import com.jhproject.confidant.ui.theme.MehLight

@Preview
@Composable
fun EntryScreen(
    darkTheme: Boolean = false,
    entryScreenViewModel: EntryScreenViewModel = viewModel(),
) {
    val background = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)

    val listState = rememberLazyListState()

    val entries by entryScreenViewModel.entries.collectAsState()

    LaunchedEffect(Unit) {
        entryScreenViewModel.loadEntries()
    }

    val isInitialLoad = entries.isEmpty()

    Surface(
        color = background, modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 88.dp
            )
        ) {
            if (!isInitialLoad) {
                items(entries, key = { it.id }) { item ->
                    EntryCard(
                        data = item, darkTheme = darkTheme
                    )
                }
            }
        }
    }
}

@Immutable
data class MoodResources(
    val icon: ImageVector,
    val label: String,
    val color: Color
)

@Composable
fun rememberMoodResources(
    mood: String,
    darkTheme: Boolean
): MoodResources {

    val label = when (mood) {
        "great" -> stringResource(R.string.great)
        "good" -> stringResource(R.string.good)
        "meh" -> stringResource(R.string.meh)
        "bad" -> stringResource(R.string.bad)
        "awful" -> stringResource(R.string.awful)
        else -> stringResource(R.string.meh)
    }

    val iconRes = when (mood) {
        "great" -> R.drawable.sentiment_excited_24px
        "good" -> R.drawable.sentiment_satisfied_24px
        "meh" -> R.drawable.sentiment_neutral_24px
        "bad" -> R.drawable.sentiment_dissatisfied_24px
        "awful" -> R.drawable.sentiment_sad_24px
        else -> R.drawable.sentiment_neutral_24px
    }

    val color = when (mood) {
        "great" -> if (darkTheme) GreatDark else GreatLight
        "good" -> if (darkTheme) GoodDark else GoodLight
        "meh" -> if (darkTheme) MehDark else MehLight
        "bad" -> if (darkTheme) BadDark else BadLight
        "awful" -> if (darkTheme) AwfulDark else AwfulLight
        else -> if (darkTheme) MehDark else MehLight
    }

    val icon = ImageVector.vectorResource(iconRes)

    return remember(label, color, icon) {
        MoodResources(icon, label, color)
    }
}

@Composable
fun EntryCard(
    data: EntryCardData,
    darkTheme: Boolean
) {
    val mood = rememberMoodResources(data.mood, darkTheme)

    val baseStyle = MaterialTheme.typography.headlineMedium
    val dateStyle = MaterialTheme.typography.titleMedium
    val timeStyle = MaterialTheme.typography.titleMedium
    val noteStyle = MaterialTheme.typography.bodyLarge

    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.widthIn(max = 600.dp)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = mood.icon,
                tint = mood.color,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Column(Modifier.weight(1f)) {
                Text(data.date, style = dateStyle)

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = mood.label,
                        style = baseStyle,
                        fontWeight = FontWeight.Bold,
                        color = mood.color,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = data.time,
                        style = timeStyle,
                        modifier = Modifier.alignByBaseline()
                    )
                }

                Text(data.note, style = noteStyle)
            }

            EntryCardMenu()
        }
    }
}

@Preview
@Composable
fun EntryCardMenu() {
    var expanded by remember { mutableStateOf(false) }

    val menuIcon = ImageVector.vectorResource(R.drawable.more_horiz_24px)
    val editIcon = ImageVector.vectorResource(R.drawable.edit_24px)
    val deleteIcon = ImageVector.vectorResource(R.drawable.delete_24px)

    Box {
        OutlinedIconButton(
            onClick = {
                expanded = !expanded
            },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = menuIcon,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.edit_description)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            DropdownMenuItem(
                leadingIcon = { Icon(editIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer) },
                text = { Text(stringResource(R.string.edit), color = MaterialTheme.colorScheme.onTertiaryContainer) },
                onClick = { expanded = false }
            )

            DropdownMenuItem(
                leadingIcon = { Icon(deleteIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer) },
                text = { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.onTertiaryContainer) },
                onClick = { expanded = false }
            )
        }
    }
}