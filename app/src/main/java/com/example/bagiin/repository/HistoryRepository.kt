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
            .sortedByDescending { it.id }
    }

    suspend fun insertHistory(aktivitas: String) {
        val userId = getCurrentUserId()
            ?: throw Exception("User belum login")

        val data = HistoryInsert(
            aktivitas = aktivitas,
            id_user = userId
        )

        client
            .from("riwayat")
            .insert(data)
    }
}