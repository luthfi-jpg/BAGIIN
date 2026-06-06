package com.example.bagiin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class JadwalPenyerahanViewModel : ViewModel() {

    var additionalInstructions by mutableStateOf("")
        private set

    var selectedDateText by mutableStateOf("11/15/2023")
        private set

    var selectedTimeText by mutableStateOf("2:00 PM")
        private set

    var showSuccessDialog by mutableStateOf(false)
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

    fun confirmSchedule() {
        showSuccessDialog = true
    }

    fun dismissDialog() {
        showSuccessDialog = false
    }
}