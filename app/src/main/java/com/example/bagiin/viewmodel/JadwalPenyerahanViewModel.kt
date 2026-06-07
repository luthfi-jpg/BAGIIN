package com.example.bagiin.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.data.model.JadwalPenyerahan
import com.example.bagiin.data.repository.JadwalRepository
import kotlinx.coroutines.launch

class JadwalPenyerahanViewModel : ViewModel() {

    private val repository = JadwalRepository()

    var additionalInstructions by mutableStateOf("")
        private set

    var selectedDateText by mutableStateOf("11/15/2023")
        private set

    var selectedTimeText by mutableStateOf("2:00 PM")
        private set

    var showSuccessDialog by mutableStateOf(false)
        private set

    var jadwalList by mutableStateOf<List<JadwalPenyerahan>>(emptyList())
        private set

    fun updateInstructions(text: String) {
        additionalInstructions = text
    }

    fun updateDate(date: String) {
        selectedDateText = date
    }

    fun updateTime(time: String) {
        selectedTimeText = time
    }

    fun saveSchedule(
        claimId: String
    ) {
        viewModelScope.launch {
            try {
                val jadwal =
                    JadwalPenyerahan(
                        id_klaim = claimId,
                        tanggal = selectedDateText,
                        waktu = selectedTimeText,
                        instruksi = additionalInstructions,
                        status = "menunggu"
                    )

                repository.insertJadwal(jadwal)
                showSuccessDialog = true
                loadSchedules()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadSchedules() {
        viewModelScope.launch {
            try {
                jadwalList = repository.getAllJadwal()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun dismissDialog() {
        showSuccessDialog = false
    }
}