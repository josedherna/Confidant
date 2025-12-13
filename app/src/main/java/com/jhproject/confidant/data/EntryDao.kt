package com.jhproject.confidant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
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
        ORDER BY entry_date DESC, entry_time DESC
    """)
    fun getEntriesForMonth(startMillis: Long, endMillis: Long): Flow<List<Entry>>

    @Query("""
        SELECT mood
        FROM entries
        WHERE entry_date >= :startMillis
          AND entry_date < :endMillis
        GROUP BY mood
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """)
    fun getMostCommonMood(startMillis: Long, endMillis: Long): Flow<String?>

    @Query("""
    SELECT mood, COUNT(*) AS count
    FROM entries
    WHERE entry_date >= :startMillis
      AND entry_date < :endMillis
    GROUP BY mood
    ORDER BY count DESC
""")
    fun getMoodCountsForMonth(
        startMillis: Long,
        endMillis: Long
    ): Flow<List<MoodCount>>

    @Query("SELECT * FROM entries WHERE id = :id LIMIT 1")
    suspend fun getEntry(id: Int): Entry?

    @Query("SELECT COUNT(*) FROM entries")
    fun getLifetimeEntriesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM entries WHERE entry_date >= :startMillis AND entry_date < :endMillis")
    fun getMonthlyEntriesCount(startMillis: Long, endMillis: Long): Flow<Int>

    @Insert
    suspend fun insertEntry(entry: Entry)

    @Update
    suspend fun updateEntry(entry: Entry)

    @Query("DELETE FROM entries WHERE id = :id")
    suspend fun deleteEntry(id: Int)

    @Query("""
    DELETE FROM entries
    WHERE entry_date >= :startMillis
      AND entry_date < :endMillis
    """)
    suspend fun deleteEntriesForMonth(
        startMillis: Long,
        endMillis: Long
    )

    @Query("DELETE FROM entries")
    suspend fun deleteAllEntries()
}