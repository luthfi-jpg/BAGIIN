package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val id: Int,
    val aktivitas: String,
    val tanggal: String
)