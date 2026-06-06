package com.example.bagiin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.model.HistoryItem
import com.example.bagiin.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val historyList: List<HistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class HistoryViewModel : ViewModel() {

    private val repository = HistoryRepository()

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                val data = repository.getHistoryByCurrentUser()

                _uiState.value = _uiState.value.copy(
                    historyList = data,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Gagal mengambil riwayat"
                )
            }
        }
    }

    fun addHistory(aktivitas: String) {
        if (aktivitas.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Aktivitas tidak boleh kosong"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                repository.insertHistory(aktivitas.trim())

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Riwayat berhasil ditambahkan"
                )

                loadHistory()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Gagal menambahkan riwayat"
                )
            }
        }
    }
}