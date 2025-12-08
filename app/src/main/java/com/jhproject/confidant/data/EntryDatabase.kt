package com.jhproject.confidant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Entry::class], version = 1)
abstract class EntryDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile private var INSTANCE: EntryDatabase? = null

        fun getInstance(context: Context): EntryDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    EntryDatabase::class.java,
                    "entries_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
