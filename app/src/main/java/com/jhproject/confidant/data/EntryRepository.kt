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

    suspend fun insertEntry(entry: Entry) {
        entryDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: Entry) {
        entryDao.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: Entry) {
        entryDao.deleteEntry(entry)
    }
}