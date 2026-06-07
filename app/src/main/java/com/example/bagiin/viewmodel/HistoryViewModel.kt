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
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class HistoryViewModel : ViewModel() {

    private val repository = HistoryRepository()

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    val filteredHistoryList: List<HistoryItem>
        get() = _uiState.value.historyList.filter { item ->

            val keyword = _uiState.value.searchQuery
                .trim()
                .lowercase()

            keyword.isBlank() ||
                    item.aktivitas.lowercase().contains(keyword) ||
                    item.judul_barang.orEmpty().lowercase().contains(keyword) ||
                    item.status.orEmpty().lowercase().contains(keyword) ||
                    item.tanggal.orEmpty().lowercase().contains(keyword)
        }

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

    fun addHistory(
        aktivitas: String,
        idDonasi: String? = null,
        judulBarang: String? = null,
        fotoUrl: String? = null,
        status: String? = null
    ) {
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
                repository.insertHistory(
                    aktivitas = aktivitas.trim(),
                    idDonasi = idDonasi,
                    judulBarang = judulBarang,
                    fotoUrl = fotoUrl,
                    status = status
                )

                val data = repository.getHistoryByCurrentUser()

                _uiState.value = _uiState.value.copy(
                    historyList = data,
                    isLoading = false,
                    successMessage = "Riwayat berhasil ditambahkan"
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Gagal menambahkan riwayat"
                )
            }

        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun updateHistoryStatus(idRiwayat: Long, status: String) {
        viewModelScope.launch {
            try {
                repository.updateHistoryStatus(idRiwayat, status)
                loadHistory()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Gagal update riwayat"
                )
            }
        }
    }

    fun deleteHistory(idRiwayat: Long) {
        viewModelScope.launch {
            try {
                repository.deleteHistory(idRiwayat)
                loadHistory()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Gagal hapus riwayat"
                )
            }
        }
    }

}