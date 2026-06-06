package com.example.bagiin.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.model.User
import com.example.bagiin.repository.ProfileRepository
import com.example.bagiin.repository.DonasiRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()
    private val donasiRepository = DonasiRepository()

    var user = mutableStateOf<User?>(null)
    var donationCount = mutableIntStateOf(0)
    var message = mutableStateOf("")
    var loading = mutableStateOf(false)
    var isEditing = mutableStateOf(false)

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            loading.value = true
            val result = repository.getProfile()
            result.onSuccess {
                user.value = it
                message.value = ""
                loadDonationCount()
            }
            result.onFailure {
                message.value = it.message ?: "Gagal memuat profil"
            }
            loading.value = false
        }
    }

    private fun loadDonationCount() {
        viewModelScope.launch {
            val result = donasiRepository.getDonasi()
            result.onSuccess { list ->
                val userId = user.value?.id_user
                donationCount.intValue = list.count { it.id_user == userId }
            }
        }
    }

    fun updateProfile(nama: String, noHp: String, alamat: String, fotoProfil: String? = null) {
        viewModelScope.launch {
            loading.value = true
            val result = repository.updateProfile(nama, noHp, alamat, fotoProfil)
            result.onSuccess {
                message.value = it
                isEditing.value = false
                loadProfile() // <- reload data setelah update
            }
            result.onFailure {
                message.value = it.message ?: "Gagal update profil"
                loading.value = false
            }
        }
    }

    fun uploadAvatar(byteArray: ByteArray, fileName: String) {
        viewModelScope.launch {
            loading.value = true
            val currentUser = user.value
            val userId: String? = currentUser?.id_user
            
            if (userId != null) {
                val uploadResult = repository.uploadAvatar(userId, byteArray, fileName)
                uploadResult.onSuccess { url ->
                    updateProfile(currentUser.nama, currentUser.no_hp ?: "", currentUser.alamat ?: "", url)
                }
                uploadResult.onFailure {
                    message.value = it.message ?: "Gagal upload foto"
                    loading.value = false
                }
            } else {
                message.value = "User tidak ditemukan"
                loading.value = false
            }
        }
    }
}