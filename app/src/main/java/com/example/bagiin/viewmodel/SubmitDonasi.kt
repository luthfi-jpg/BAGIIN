package com.example.bagiin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagiin.repository.DonasiRepository
import kotlinx.coroutines.launch

class DonasiViewModel : ViewModel() {

    private val repository = DonasiRepository()

    fun submitDonasi(
        judul: String,
        deskripsi: String,
        kategori: String,
        kondisi: String,
        lokasi: String
    ) {
        viewModelScope.launch {

            repository.insertDonasi(
                judul = judul,
                deskripsi = deskripsi,
                kategori = kategori,
                kondisi = kondisi,
                lokasi = lokasi
            )

        }
    }
}