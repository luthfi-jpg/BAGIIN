package com.example.bagiin.data.model

import io.github.jan.supabase.storage.UploadStatus
import kotlinx.serialization.Serializable

//@Serializable
//data class JadwalPenyerahan(
//    val id_jadwal: String? = null,
//    val id_klaim: String,
//    val tanggal: String,
//    val lokasi: String,
//    val status: String
//)

@Serializable
data class JadwalPenyerahan(
    val id_jadwal: String? = null,
    val id_klaim: String,
    val tanggal: String,
    val waktu: String? = null,
    val instruksi: String? = null,
    val status: String? = null
)
