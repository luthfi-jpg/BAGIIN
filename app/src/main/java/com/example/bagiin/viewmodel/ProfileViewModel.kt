package com.example.bagiin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.model.User
import com.example.bagiin.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    var user = mutableStateOf<User?>(null)
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
            }
            result.onFailure {
                message.value = it.message ?: "Gagal memuat profil"
            }
            loading.value = false
        }
    }

    fun updateProfile(nama: String, noHp: String, alamat: String) {
        viewModelScope.launch {
            loading.value = true
            val result = repository.updateProfile(nama, noHp, alamat)
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
}