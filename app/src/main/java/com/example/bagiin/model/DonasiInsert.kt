package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class DonasiInsert(
    val judul: String,
    val deskripsi: String? = null,
    val kategori: String? = null,
    val kondisi: String? = null,
    val lokasi: String? = null,
    val status: String = "tersedia",
    val foto_url: List<String> = emptyList()
)