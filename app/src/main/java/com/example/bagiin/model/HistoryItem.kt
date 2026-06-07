package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val id_riwayat: Long = 0,
    val id_user: String,
    val id_donasi: String? = null,
    val aktivitas: String = "",
    val judul_barang: String? = null,
    val foto_url: String? = null,
    val status: String? = null,
    val tanggal: String? = null
)

@Serializable
data class HistoryInsert(
    val id_user: String,
    val id_donasi: String? = null,
    val aktivitas: String,
    val judul_barang: String? = null,
    val foto_url: String? = null,
    val status: String? = null
)