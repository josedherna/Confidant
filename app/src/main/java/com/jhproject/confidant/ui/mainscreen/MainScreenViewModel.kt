package com.jhproject.confidant.ui.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainScreenViewModel : ViewModel() {
    val currentDate: LocalDate = LocalDate.now()
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    val formattedCurrentDate: String = currentDate.format(formatter)

    var selectedDate: String by mutableStateOf(formattedCurrentDate)
}