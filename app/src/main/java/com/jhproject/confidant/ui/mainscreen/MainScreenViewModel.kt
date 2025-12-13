package com.jhproject.confidant.ui.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhproject.confidant.data.Entry
import com.jhproject.confidant.data.EntryRepository
import com.jhproject.confidant.data.MonthYear
import com.jhproject.confidant.data.MoodCount
import com.jhproject.confidant.data.toCardData
import com.jhproject.confidant.ui.entryscreen.EntryCardData
import com.jhproject.confidant.ui.entryscreen.Mood
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    //Keeps track on whether the months list has been initialized when app first opens
    private var initialized = false

    //Ensures that the selected month is initialized and survives configuration changes
    fun displaySelectedMonth(months: List<MonthYear>) {
        if (!initialized) {
            selectedMonth.value = months.firstOrNull() ?: currentMonthYear()
            initialized = true
            return
        }

        if (months.isEmpty()) {
            return
        }

        val current = selectedMonth.value

        //If current is month still exists in database, do nothing
        if (current != null && current in months) return

        //If current is not in database, select next available month which is in the end of months list
        selectedMonth.value = months.last()
    }

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val mostCommonMood: StateFlow<String?> = initSelectedMonth
        .flatMapLatest { month ->
            val (start, end) = monthRange(month ?: currentMonthYear())
            repository.getMostCommonMood(start, end)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
    val lifetimeEntryCount: Flow<Int> = repository.getLifetimeEntriesCount()

    @OptIn(ExperimentalCoroutinesApi::class)
    val monthlyEntryCount: StateFlow<Int> = initSelectedMonth
        .flatMapLatest { month ->
            val (start, end) = monthRange(month ?: currentMonthYear())
            repository.getMonthlyEntriesCount(start, end)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    //Saves entry to the database, as an entry entity
    fun saveEntry(entry: Entry) {
        viewModelScope.launch {
            repository.insertEntry(entry)
        }
    }

    //Gets an entry to be edited
    private val entry = MutableStateFlow<Entry?>(null)
    val initEntry: StateFlow<Entry?> = entry

    //Used to update an entry in the database using its id
    private val entryID = MutableStateFlow<Int?>(null)
    val initEntryID: StateFlow<Int?> = entryID

    fun setEntryID(id: Int?) {
        entryID.value = id
    }

    fun getEntry(id: Int) {
        viewModelScope.launch {
            entry.value = repository.getEntry(id)
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: StateFlow<List<EntryCardData>> =
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    repository.searchEntries(query)
                }
            }
            .map { entries -> entries.map { it.toCardData() } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    //Updates entry in the database based on the entry entity
    fun updateEntry(entry: Entry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    //Deletes an entry from the database based on its ID
    fun deleteEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteEntry(id)
        }
    }

    //Flow that stores a users selection for mood
    private val selectedMoodCreationId = MutableStateFlow<Int?>(null)
    val initSelectedMoodCreationID: StateFlow<Int?> = selectedMoodCreationId.asStateFlow()

    fun setSelectedMoodCreationID(id: Int?) {
        selectedMoodCreationId.value = id
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val moodCounts: StateFlow<List<MoodCount>> =
        initSelectedMonth
            .filterNotNull()
            .flatMapLatest { month ->
                val (start, end) = monthRange(month)
                repository.getMoodCountsForMonth(start, end)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun deleteCurrentMonthEntries() {
        val month = selectedMonth.value ?: currentMonthYear()
        val (start, end) = monthRange(month)

        viewModelScope.launch {
            repository.deleteEntriesForMonth(start, end)
        }
    }

    fun deleteAllJournalEntries() {
        viewModelScope.launch {
            repository.deleteAllEntries()
        }
    }
}