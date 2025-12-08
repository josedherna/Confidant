package com.jhproject.confidant.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Month

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "entry_date")
    val entryDate: Long,

    @ColumnInfo(name = "entry_time")
    val entryTime: Long,

    val mood: String,
    val notes: String
)

data class MonthYear(
    val year: String,
    val month: String
) {
    fun label(): String {
        val monthInt = month.toInt()
        return "${Month.of(monthInt).name.lowercase().replaceFirstChar { it.uppercase() }} $year"
    }
}