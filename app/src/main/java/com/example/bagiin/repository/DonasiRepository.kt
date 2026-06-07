package com.example.bagiin.repository

import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.DonasiInsert
import io.github.jan.supabase.postgrest.from

class DonasiRepository {

    private val client = SupabaseInstance.client

    suspend fun insertDonasi(
        judul: String,
        deskripsi: String,
        kategori: String,
        kondisi: String,
        lokasi: String
    ) {

        val data = DonasiInsert(
            judul = judul,
            deskripsi = deskripsi,
            kategori = kategori,
            kondisi = kondisi,
            lokasi = lokasi
        )

        client
            .from("donasi")
            .insert(data)
    }
}