package com.example.bagiin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.model.Claim
import com.example.bagiin.model.Donasi
import com.example.bagiin.repository.RiwayatRepository
import kotlinx.coroutines.launch

class RiwayatViewModel : ViewModel() {
    private val repository = RiwayatRepository()

    var myDonations = mutableStateOf<List<Donasi>>(emptyList())
    var myClaims = mutableStateOf<List<Claim>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun fetchRiwayat() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val donationsResult = repository.getMyDonations()
            donationsResult.onSuccess { myDonations.value = it }
            donationsResult.onFailure { errorMessage.value = it.message }

            val claimsResult = repository.getMyClaims()
            claimsResult.onSuccess { myClaims.value = it }
            claimsResult.onFailure { errorMessage.value = it.message }

            isLoading.value = false
        }
    }

    fun confirmAndRate(idKlaim: String, idDonasi: String, rating: Double, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.confirmReceipt(idKlaim, idDonasi, rating)
            result.onSuccess {
                fetchRiwayat()
                onSuccess()
            }
            result.onFailure {
                errorMessage.value = it.message ?: "Gagal menyelesaikan klaim"
            }
            isLoading.value = false
        }
    }
}
