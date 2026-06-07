package com.example.bagiin.repository

import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.HistoryInsert
import com.example.bagiin.model.HistoryItem
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class HistoryRepository {

    private val client = SupabaseInstance.client

    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    suspend fun getHistoryByCurrentUser(): List<HistoryItem> {
        val userId = getCurrentUserId()
            ?: throw Exception("User belum login")

        return client
            .from("riwayat")
            .select {
                filter {
                    eq("id_user", userId)
                }
            }
            .decodeList<HistoryItem>()
            .sortedByDescending { it.id_riwayat }
    }

    suspend fun insertHistory(
        aktivitas: String,
        idDonasi: String? = null,
        judulBarang: String? = null,
        fotoUrl: String? = null,
        status: String? = null
    ) {
        val userId = getCurrentUserId()
            ?: throw Exception("User belum login")

        val data = HistoryInsert(
            id_user = userId,
            id_donasi = idDonasi,
            aktivitas = aktivitas,
            judul_barang = judulBarang,
            foto_url = fotoUrl,
            status = status
        )

        client
            .from("riwayat")
            .insert(data)
    }
}