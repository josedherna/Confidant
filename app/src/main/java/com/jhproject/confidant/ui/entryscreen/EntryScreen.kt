package com.jhproject.confidant.ui.entryscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhproject.confidant.R
import com.jhproject.confidant.data.Entry
import com.jhproject.confidant.ui.mainscreen.MainScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun EntryScreen(
    darkTheme: Boolean = false,
    mainScreenViewModel: MainScreenViewModel,
) {
    val background = if (darkTheme) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)

    val listState = rememberLazyListState()

    val entries by mainScreenViewModel.entryCards.collectAsState()

    val isInitialLoad = entries.isEmpty()

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
            if (!isInitialLoad) {
                items(entries, key = { it.id }) { item ->
                    EntryCard(
                        data = item,
                        darkTheme = darkTheme
                    )
                }
            }
        }
    }
}

private val moodIcons = Mood.entries

@Composable
fun EntryCard(
    data: EntryCardData,
    darkTheme: Boolean
) {
    val mood = Mood.fromKey(data.mood)

    val label = stringResource(mood.label)
    val icon = rememberVectorPainter(ImageVector.vectorResource(mood.icon))

    val color = if (darkTheme) mood.darkColor else mood.lightColor

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
                painter = icon,
                tint = color,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Column(Modifier.weight(1f)) {
                Text(data.date, style = dateStyle)

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = label,
                        style = baseStyle,
                        fontWeight = FontWeight.Bold,
                        color = color,
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

    val menuIcon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.more_horiz_24px))
    val editIcon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.edit_24px))
    val deleteIcon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.delete_24px))

    Box {
        OutlinedIconButton(
            onClick = {
                expanded = !expanded
            },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = menuIcon,
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

@Composable
fun MoodButton(
    id: Int,
    viewModel: MainScreenViewModel,
    mood: Mood,
    darkTheme: Boolean,
) {
    val icon = rememberVectorPainter(ImageVector.vectorResource(mood.icon))
    val filledIcon = rememberVectorPainter(ImageVector.vectorResource(mood.iconFilled))
    val label = stringResource(mood.label)

    val color = if (darkTheme) mood.darkColor else mood.lightColor

    val isSelected by viewModel.selectedMoodCreationID
        .map { it == id }
        .collectAsState(initial = false)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IconButton(
            onClick = {
                viewModel.setSelectedMoodCreationID(id)
            },
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                painter = if (isSelected) filledIcon else icon,
                tint = color,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun MoodButtonRow(
    viewModel: MainScreenViewModel,
    darkTheme: Boolean,
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        moodIcons.forEach { mood ->
            MoodButton(
                darkTheme = darkTheme,
                id = mood.ordinal,
                viewModel = viewModel,
                mood = mood
            )
        }
    }
}

@Composable
fun DateField(
    viewModel: MainScreenViewModel,
    modifier: Modifier
) {
    val icon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.otherday_24px))
    val label = stringResource(R.string.date_label)

    LaunchedEffect(Unit) {
        if (viewModel.selectedDateMillis == null) {
            val default = LocalDate.now().atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
            viewModel.setSelectedDate(default)
        }
    }

    var selectedDate by remember {
        mutableLongStateOf(
            viewModel.selectedDateMillis ?: LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = convertMillisToDate(selectedDate),
            onValueChange = { },
            enabled = false,
            label = {
                Text(
                    text = label
                )
            },
            leadingIcon = {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            readOnly = true,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = modifier
                .clickable {
                    showDatePicker = true
                }
        )
        if (showDatePicker) {
            ModalDatePicker(
                onDateSelected = { millis ->
                    val safeMillis = millis ?: selectedDate
                    viewModel.setSelectedDate(safeMillis)
                    selectedDate = safeMillis },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }

}

fun convertMillisToDate(millis: Long): String {
    val localDate = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    return localDate.format(formatter)
}

@Composable
fun ModalDatePicker(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val todayUTC = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= todayUTC
            }
        }
    )

    if (datePickerState.displayMode != DisplayMode.Input) {
        datePickerState.displayMode = DisplayMode.Input
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToTime(millis: Long): String {
    val time = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return time.format(formatter)
}

fun millisToLocalTime(millis: Long): LocalTime {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
}

fun localTimeToMillis(selectedDateMillis: Long, hour: Int, minute: Int): Long {
    val date = Instant.ofEpochMilli(selectedDateMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return LocalDateTime.of(date, LocalTime.of(hour, minute))
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (viewModel.selectedTimeMillis == null) {
            val default = System.currentTimeMillis()
            viewModel.setSelectedTime(default)
        }
    }

    //Loads from ViewModel as a Long
    val storedTimeMillis = viewModel.selectedTimeMillis

    //Converts to string
    var selectedTimeText by remember(storedTimeMillis) {
        mutableStateOf(
            convertMillisToTime(storedTimeMillis ?: System.currentTimeMillis())
        )
    }

    val icon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.schedule_24px))
    val label = stringResource(R.string.time_label)

    OutlinedTextField(
        value = selectedTimeText,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        enabled = false,
        leadingIcon = {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true }
    )

    if (showTimePicker) {
        //Extracts initial hour/minute for timepicker dialog
        val initial = millisToLocalTime(storedTimeMillis ?: System.currentTimeMillis())

        val timePickerState = rememberTimePickerState(
            initialHour = initial.hour,
            initialMinute = initial.minute,
            is24Hour = false
        )

        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Build new time as millis
                        val millis = localTimeToMillis(
                            selectedDateMillis = viewModel.selectedDateMillis ?: System.currentTimeMillis(),
                            hour = timePickerState.hour,
                            minute = timePickerState.minute
                        )

                        //Stores in ViewModel
                        viewModel.setSelectedTime(millis)

                        selectedTimeText = convertMillisToTime(millis)

                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            },
            title = { }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
fun EntryNoteField(
    onHeightChanged: (Int) -> Unit,
    mainScreenViewModel: MainScreenViewModel
) {
    val text by mainScreenViewModel.textFieldValue.collectAsState()
    val label = stringResource(R.string.entry_note_prompt)

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                mainScreenViewModel.onTextFieldValueChange(newValue)
            },
            label = {
                Text(
                    text = label
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    onHeightChanged(size.height)
                }
        )
}

@Composable
fun EntryDateTimeRow(
    viewModel: MainScreenViewModel
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.widthIn(max = 600.dp)
    ) {
        DateField(
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
        TimeField(
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryCreationBottomSheet(
    onDismissRequest: () -> Unit,
    mainScreenViewModel: MainScreenViewModel,
    darkTheme: Boolean,
    sheetState: SheetState,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val listState = rememberLazyListState()
    var lastHeight by remember { mutableIntStateOf(0) }
    var currentHeight by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentHeight) {
        val delta = currentHeight - lastHeight
        if (delta > 0) {
            listState.scrollBy(delta.toFloat())
        }
        lastHeight = currentHeight
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier.statusBarsPadding()
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = screenHeight, screenHeight)
            ) {
                LazyColumn(
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    verticalArrangement = Arrangement.spacedBy(46.dp)
                ) {
                    item {
                        TopAppBar(
                            title = {
                                Text(
                                    text = ""
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = onDismissRequest
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.close_24px),
                                        contentDescription = stringResource(R.string.close)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        )
                    }
                    item {
                        Text(
                            text = stringResource(R.string.entry_creation_prompt),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        MoodButtonRow(mainScreenViewModel, darkTheme)
                    }

                    item {
                        EntryDateTimeRow(
                            viewModel = mainScreenViewModel
                        )
                    }

                    item {
                        Box(
                            Modifier.heightIn(max = 600.dp)
                        ) {
                            EntryNoteField(
                                onHeightChanged = { newHeight ->
                                    currentHeight = newHeight
                                },
                                mainScreenViewModel = mainScreenViewModel
                            )
                        }
                    }

                    item {
                        SaveButton(
                            mainScreenViewModel,
                            onDismissRequest = onDismissRequest
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(0.dp))
                    }
                }
            }
    }
}

@Composable
fun SaveButton(
    viewModel: MainScreenViewModel,
    onDismissRequest: () -> Unit,
) {
    val saveButtonStyle = MaterialTheme.typography.headlineSmall

    val selectedMoodId by viewModel.selectedMoodCreationID.collectAsState()
    val moodSelected = selectedMoodId != null

    Button(
        onClick = {
            viewModel.saveEntry(
                Entry(
                    mood = viewModel.getMoodByID(viewModel.selectedMoodCreationID.value ?: 0).key,
                    entryDate = viewModel.selectedDateMillis ?: 0,
                    entryTime = viewModel.selectedTimeMillis ?: 0,
                    notes = viewModel.textFieldValue.value
                )
            )
            onDismissRequest()
        },
        enabled = moodSelected,
        modifier = Modifier
            .width(214.dp)
            .height(96.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.check_24px),
            contentDescription = stringResource(R.string.save_description),
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.save_label),
            style = saveButtonStyle
        )
    }
}
