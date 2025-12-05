package com.jhproject.confidant.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface EntryDao {
    @Query("""
        SELECT 
            strftime('%Y', entry_date / 1000, 'unixepoch') AS year,
            strftime('%m', entry_date / 1000, 'unixepoch') AS month
        FROM entries
        GROUP BY year, month
        ORDER BY year DESC, month DESC
    """)
    fun getAvailableMonths(): Flow<List<MonthYear>>

    @Query("""
        SELECT * FROM entries
        WHERE entry_date >= :startMillis
          AND entry_date < :endMillis
        ORDER BY entry_time DESC
    """)
    fun getEntriesForMonth(startMillis: Long, endMillis: Long): Flow<List<Entry>>

    @Insert
    fun insertEntry(entry: Entry)

    @Update
    fun updateEntry(entry: Entry)

    @Delete
    fun deleteEntry(entry: Entry)
}