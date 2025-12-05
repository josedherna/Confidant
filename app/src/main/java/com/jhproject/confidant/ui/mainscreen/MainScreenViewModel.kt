package com.jhproject.confidant.ui.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jhproject.confidant.ui.entryscreen.Mood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class MainScreenViewModel : ViewModel() {
    val currentDate: LocalDate = LocalDate.now()
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    val formattedCurrentDate: String = currentDate.format(formatter)

    var selectedDate: String by mutableStateOf(formattedCurrentDate)

    private val _selectedMoodCreationId = MutableStateFlow<Int?>(null)
    val selectedMoodCreationID: StateFlow<Int?> = _selectedMoodCreationId.asStateFlow()

    private val _textFieldValue = MutableStateFlow("")
    val textFieldValue: StateFlow<String> = _textFieldValue.asStateFlow()

    fun onTextFieldValueChange(newValue: String) {
        _textFieldValue.value = newValue
    }

    var selectedDateMillis by mutableStateOf<Long?>(null)
        private set

    fun setSelectedDate(millis: Long?) {
        selectedDateMillis = millis
    }

    var selectedTimeMillis by mutableStateOf<Long?>(null)
        private set

    fun setSelectedTime(millis: Long) {
        selectedTimeMillis = millis
    }

    fun setSelectedMoodCreationID(id: Int?) {
        _selectedMoodCreationId.value = id
    }

    fun getMoodByID(id: Int): Mood {
        return Mood.entries[id]
    }
}