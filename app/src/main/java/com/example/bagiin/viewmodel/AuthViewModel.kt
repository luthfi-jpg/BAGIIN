package com.example.bagiin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    var message = mutableStateOf("")
    var loading = mutableStateOf(false)

    fun register(
        nama: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {

            loading.value = true

            val result = repository.register(
                nama,
                email,
                password
            )

            loading.value = false

            result.onSuccess {
                message.value = it
            }

            result.onFailure {
                message.value = it.message ?: "Terjadi error"
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {

            loading.value = true

            val result = repository.login(
                email,
                password
            )

            loading.value = false

            result.onSuccess {
                message.value = it
            }

            result.onFailure {
                message.value = it.message ?: "Terjadi error"
            }
        }
    }
}