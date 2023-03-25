package com.android.roomdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private var historyDao: HistoryDao? = null

    init {
        historyDao = HistoryDatabase.getInstance(application).historyDao()
    }

    fun addHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            historyDao?.insert(historyEntity)
        }
    }

    fun fetchAllHistory(): Flow<List<HistoryEntity>>? {
        return historyDao?.fetchAllHistory()
    }
}