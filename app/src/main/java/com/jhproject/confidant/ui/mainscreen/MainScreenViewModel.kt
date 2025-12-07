package com.jhproject.confidant.ui.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhproject.confidant.data.Entry
import com.jhproject.confidant.data.EntryRepository
import com.jhproject.confidant.data.MonthYear
import com.jhproject.confidant.data.toCardData
import com.jhproject.confidant.ui.entryscreen.EntryCardData
import com.jhproject.confidant.ui.entryscreen.Mood
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId


class MainScreenViewModel(private val repository : EntryRepository) : ViewModel() {
    //Flows that store a user's selected month
    private val selectedMonth = MutableStateFlow<MonthYear?>(null)
    val initSelectedMonth: StateFlow<MonthYear?> = selectedMonth

    val availableMonths: Flow<List<MonthYear>> = repository.getAvailableMonths()

    fun setSelectedMonth(month: MonthYear) {
        selectedMonth.value = month
    }

    //Helper function to
    private fun monthRange(monthYear: MonthYear): Pair<Long, Long> {
        val year = monthYear.year.toInt()
        val month = monthYear.month.toInt()
        val start = LocalDate.of(year, month, 1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val end = LocalDate.of(year, month, 1)
            .plusMonths(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return start to end
    }

    //Gets the current month and year, the default if a user has not selected a month
    fun currentMonthYear(): MonthYear {
        val now = LocalDate.now()
        return MonthYear(now.year.toString(), now.monthValue.toString().padStart(2, '0'))
    }

    //Based on the selected month, the flow will emit a list of entries for that month
    fun loadEntriesForCurrentMonth(): Flow<List<Entry>> {
        val month = selectedMonth.value ?: currentMonthYear()
        val (start, end) = monthRange(month)
        return repository.getEntriesForMonth(start, end)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val entryCards: StateFlow<List<EntryCardData>> =
        initSelectedMonth
            .flatMapLatest { loadEntriesForCurrentMonth() }
            .map { entries -> entries.map { it.toCardData() } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    //Saves entry to the room db, as an entry entity
    fun saveEntry(entry: Entry) {
        viewModelScope.launch {
            repository.insertEntry(entry)
        }
    }

    //Flow that stores a users selection for mood
    private val _selectedMoodCreationId = MutableStateFlow<Int?>(null)
    val selectedMoodCreationID: StateFlow<Int?> = _selectedMoodCreationId.asStateFlow()

    fun setSelectedMoodCreationID(id: Int?) {
        _selectedMoodCreationId.value = id
    }

    fun getMoodByID(id: Int): Mood {
        return Mood.entries[id]
    }

    //Flow that stores a user's input for the note in an entry
    private val _textFieldValue = MutableStateFlow("")
    val textFieldValue: StateFlow<String> = _textFieldValue.asStateFlow()

    fun onTextFieldValueChange(newValue: String) {
        _textFieldValue.value = newValue
    }

    //Flow that stores a user's selected date for an entry as a Long in milliseconds
    var selectedDateMillis by mutableStateOf<Long?>(null)
        private set

    fun setSelectedDate(millis: Long?) {
        selectedDateMillis = millis
    }

    //Flow that store a user's selected time for an entry as a Long in milliseconds
    var selectedTimeMillis by mutableStateOf<Long?>(null)
        private set

    fun setSelectedTime(millis: Long?) {
        selectedTimeMillis = millis
    }
}