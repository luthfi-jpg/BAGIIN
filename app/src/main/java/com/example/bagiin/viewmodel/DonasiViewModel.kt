package com.example.bagiin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.model.Donasi
import com.example.bagiin.repository.DonasiRepository
import io.github.jan.supabase.auth.auth
import com.example.bagiin.data.SupabaseInstance
import kotlinx.coroutines.launch

class DonasiViewModel : ViewModel() {
    private val repository = DonasiRepository()
    private val client = SupabaseInstance.client

    var donationList = mutableStateOf<List<Donasi>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var donationDetail = mutableStateOf<Donasi?>(null)
    var message = mutableStateOf("")

    fun deleteDonasi(idDonasi: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.deleteDonasi(idDonasi)
            result.onSuccess {
                fetchDonasi()
                onSuccess()
            }
            result.onFailure {
                errorMessage.value = it.message ?: "Gagal menghapus donasi"
            }
            isLoading.value = false
        }
    }

    init {
        fetchDonasi()
    }

    fun fetchDonasi() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val result = repository.getDonasi()
            result.onSuccess { list ->
                donationList.value = list
            }
            result.onFailure { error ->
                errorMessage.value = error.message ?: "Gagal memuat data donasi"
            }

            isLoading.value = false
        }
    }

    fun uploadDonasi(
        judul: String,
        deskripsi: String,
        kategori: String,
        kondisi: String,
        lokasi: String,
        imageByteArrays: List<ByteArray>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                val userId = client.auth.currentUserOrNull()?.id
                    ?: throw Exception("User tidak ditemukan")

                var fotoUrls: List<String>? = null
                if (imageByteArrays.isNotEmpty()) {
                    val uploadResult = repository.uploadMultipleImages(userId, imageByteArrays)
                    fotoUrls = uploadResult.getOrThrow()
                }

                val donasi = Donasi(
                    id_user = userId,
                    judul = judul,
                    deskripsi = deskripsi,
                    kategori = kategori,
                    kondisi = kondisi,
                    lokasi = lokasi,
                    foto_url = fotoUrls,
                    status = "tersedia"
                )

                val result = repository.insertDonasi(donasi)
                result.onSuccess {
                    message.value = it
                    fetchDonasi()
                    onSuccess()
                }
                result.onFailure { throw it }

            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Gagal upload donasi"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun claimDonation(idDonasi: String, alasan: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            val result = repository.claimDonasi(idDonasi, alasan)
            result.onSuccess {
                message.value = it
                onSuccess()
            }
            result.onFailure { error ->
                errorMessage.value = error.message ?: "Gagal mengajukan klaim"
            }
            isLoading.value = false
        }
    }

    fun getDonationById(idDonasi: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getDonasiById(idDonasi)
            result.onSuccess {
                donationDetail.value = it
            }
            result.onFailure { error ->
                errorMessage.value = error.message ?: "Gagal memuat detail donasi"
            }
            isLoading.value = false
        }
    }
}
