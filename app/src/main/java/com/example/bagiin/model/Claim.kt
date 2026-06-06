package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class Claim(
    val id_klaim: String? = null,
    val id_donasi: String? = null,
    val id_user: String? = null,
    val status: String? = "Pending",
    val created_at: String? = null,
    val alasan: String? = null,
    val foto_url: String? = null
)
