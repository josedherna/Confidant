package com.jhproject.confidant.data

import kotlinx.coroutines.flow.Flow

class EntryRepository(
    private val entryDao: EntryDao
) {
    fun getAvailableMonths(): Flow<List<MonthYear>> {
        return entryDao.getAvailableMonths()
    }

    fun getEntriesForMonth(startMillis: Long, endMillis: Long): Flow<List<Entry>> {
        return entryDao.getEntriesForMonth(startMillis, endMillis)
    }

    fun getMostCommonMood(startMillis: Long, endMillis: Long): Flow<String?> {
        return entryDao.getMostCommonMood(startMillis, endMillis)
    }

    suspend fun getEntry(id: Int): Entry? {
        return entryDao.getEntry(id)
    }

    suspend fun insertEntry(entry: Entry) {
        entryDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: Entry) {
        entryDao.updateEntry(entry)
    }

    suspend fun deleteEntry(id: Int) {
        entryDao.deleteEntry(id)
    }
}