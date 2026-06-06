package com.example.bagiin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.model.HistoryItem
import com.example.bagiin.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val repository = HistoryRepository()

    private val _history =
        MutableStateFlow<List<HistoryItem>>(emptyList())

    val history: StateFlow<List<HistoryItem>>
            = _history

    init {
        loadHistory()
    }

    fun loadHistory() {

        viewModelScope.launch {

            try {

                _history.value =
                    repository.getHistory()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addHistory(
        idUser: String,
        aktivitas: String
    ) {

        viewModelScope.launch {

            try {

                repository.insertHistory(
                    idUser,
                    aktivitas
                )

                loadHistory()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}