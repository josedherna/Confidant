package com.jhproject.confidant.ui.entryscreen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@Immutable
data class EntryCardData(
    val id: Int,
    val date: String,
    val time: String,
    val note: String,
    val mood: String
)

class EntryScreenViewModel : ViewModel() {
    private val _entries = MutableStateFlow<List<EntryCardData>>(emptyList())
    val entries: StateFlow<List<EntryCardData>> = _entries

    //Flag to ensure data fetching only happens once per ViewModel lifecycle
    @OptIn(ExperimentalAtomicApi::class)
    private val hasLoaded = AtomicBoolean(false)

    @OptIn(ExperimentalAtomicApi::class)
    fun loadEntries() {
        // Use compareAndSet for thread-safe check-and-set to ensure single load
        if (hasLoaded.compareAndSet(expectedValue = false, newValue = true)) {
            viewModelScope.launch {
                kotlinx.coroutines.delay(230)

                // 2. Update the state, triggering a UI recomposition
                _entries.value = EntryCardList
            }
        }
    }
}

val EntryCardList = listOf(
    EntryCardData(
        id = 1,
        date = "Tuesday, November 18",
        time = "10:00 PM",
        note = "Sample note data. It will change depending on user mood.",
        mood = "bad"
    ),
    EntryCardData(
        id = 2,
        date = "Tuesday, November 18",
        time = "10:00 PM",
        note = "Sample note data. It will change depending on user mood.",
        mood = "great",
    ),
    EntryCardData(
        id = 3,
        date = "Tuesday, November 18",
        time = "10:00 PM",
        note = "Sample note data. It will change depending on user mood.",
        mood = "good",
    ),
    EntryCardData(
        id = 4,
        date = "Tuesday, November 18",
        time = "10:00 PM",
        note = "Sample note data. It will change depending on user mood.",
        mood = "meh",
    ),
    EntryCardData(
        id = 5,
        date = "Tuesday, November 18",
        time = "10:00 PM",
        note = "Sample note data. It will change depending on user mood.",
        mood = "bad",
    ),
    EntryCardData(
        id = 6,
        date = "Tuesday, November 18",
        time = "10:00 PM",
        note = "Sample note data. It will change depending on user mood.",
        mood = "awful",
    )
)