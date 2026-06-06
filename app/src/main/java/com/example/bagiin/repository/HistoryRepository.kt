package com.example.bagiin.repository

import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.HistoryItem
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.order

class HistoryRepository {

    suspend fun getHistory(): List<HistoryItem> {

        return SupabaseInstance.client
            .from("riwayat")
            .select {
                order(
                    column = "tanggal",
                    order = Order.DESCENDING
                )
            }
            .decodeList<HistoryItem>()
    }

    suspend fun insertHistory(
        idUser: String,
        aktivitas: String
    ) {

        val data = mapOf(
            "id_user" to idUser,
            "aktivitas" to aktivitas
        )

        SupabaseInstance.client
            .from("riwayat")
            .insert(data)
    }
}