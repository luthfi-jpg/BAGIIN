package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class Donasi(
    val id_donasi: String? = null,
    val id_user: String? = null,
    val judul: String? = null,
    val deskripsi: String? = null,
    val kategori: String? = null,
    val kondisi: String? = null,
    val foto_url: List<String>? = null,
    val status: String? = "tersedia",
    val rating: Double? = null,
    val created_at: String? = null,
    val lokasi: String? = null,
    val alasan: String? = null,
    val avatar_url: String? = null,
    val donor: Donor? = null
)

@Serializable
data class Donor(
    val nama: String? = null,
    val foto_profil: String? = null
)
