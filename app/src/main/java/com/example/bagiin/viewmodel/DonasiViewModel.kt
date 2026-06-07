package com.example.bagiin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.repository.DonasiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DonasiUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class DonasiViewModel : ViewModel() {

    private val repository = DonasiRepository()

    private val _uiState = MutableStateFlow(DonasiUiState())
    val uiState: StateFlow<DonasiUiState> = _uiState.asStateFlow()

    fun submitDonasi(
        judul: String,
        deskripsi: String,
        kategori: String,
        kondisi: String,
        lokasi: String,
        fotoUrl: List<String> = emptyList()
    ) {
        if (judul.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Nama barang tidak boleh kosong"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = DonasiUiState(isLoading = true)

            try {
                repository.insertDonasi(
                    judul = judul.trim(),
                    deskripsi = deskripsi.trim(),
                    kategori = kategori,
                    kondisi = kondisi,
                    lokasi = lokasi.trim(),
                    fotoUrl = fotoUrl
                )

                _uiState.value = DonasiUiState(isSuccess = true)

            } catch (e: Exception) {
                _uiState.value = DonasiUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Gagal submit donasi"
                )
            }
        }
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}