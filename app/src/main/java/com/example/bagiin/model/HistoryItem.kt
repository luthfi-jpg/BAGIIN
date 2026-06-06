package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val id: Long = 0,
    val aktivitas: String,
    val tanggal: String? = null,
    val id_user: String? = null
)