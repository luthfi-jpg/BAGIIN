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
                order("tanggal", Order.DESCENDING)
            }
            .decodeList<HistoryItem>()
    }
}