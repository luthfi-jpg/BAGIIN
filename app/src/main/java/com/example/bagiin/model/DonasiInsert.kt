package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class DonasiInsert(
    val judul: String,
    val deskripsi: String? = null,
    val kategori: String? = null,
    val kondisi: String? = null,
    val lokasi: String? = null,

    // sesuai default di database
    val status: String = "tersedia",

    // jsonb [] pada Supabase
    val foto_url: List<String> = emptyList()
)