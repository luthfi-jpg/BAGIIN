package com.example.bagiin.data.repository
import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.data.model.JadwalPenyerahan
import io.github.jan.supabase.postgrest.from

class JadwalRepository {
    private val client = SupabaseInstance.client
    suspend fun insertJadwal(jadwal: JadwalPenyerahan) {

        client
            .from("jadwal")
            .insert(jadwal)
    }

    suspend fun getAllJadwal(): List<JadwalPenyerahan> {

        return client
            .from("jadwal")
            .select()
            .decodeList<JadwalPenyerahan>()
    }
}
