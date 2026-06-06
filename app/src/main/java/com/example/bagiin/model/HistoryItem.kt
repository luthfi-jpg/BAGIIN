package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val id: Long = 0,
    val aktivitas: String = "",
    val tanggal: String? = null,
    val id_user: String? = null
)

@Serializable
data class HistoryInsert(
    val aktivitas: String,
    val id_user: String
)