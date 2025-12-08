package com.jhproject.confidant.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jhproject.confidant.ui.mainscreen.MainScreenViewModel

class EntryViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = EntryDatabase.getInstance(app)

        return when (modelClass) {

            MainScreenViewModel::class.java -> {
                val repo = EntryRepository(database.entryDao())
                MainScreenViewModel(repo) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}