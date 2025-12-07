package com.jhproject.confidant.data

import com.jhproject.confidant.ui.entryscreen.EntryCardData
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Entry.toCardData(): EntryCardData {
    //Converts entryDate to LocalDate
    val date = Instant.ofEpochMilli(entryDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val formattedDate = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))

    //Converts entryTime to LocalTime
    val time = Instant.ofEpochMilli(entryTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    val formattedTime = time.format(DateTimeFormatter.ofPattern("hh:mm a"))

    //EntryCardData will be used to create entry cards
    return EntryCardData(
        id = id,
        date = formattedDate,
        time = formattedTime,
        note = notes,
        mood = mood
    )
}