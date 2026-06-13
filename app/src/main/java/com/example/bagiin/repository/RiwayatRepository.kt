package com.example.bagiin.repository

import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.Claim
import com.example.bagiin.model.Donasi
import com.example.bagiin.model.User
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class RiwayatRepository {
    private val client = SupabaseInstance.client

    suspend fun getMyDonations(): Result<List<Donasi>> {
        return try {
            val userId = client.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("User tidak ditemukan"))

            val result = client.from("donasi")
                .select(Columns.ALL) {
                    filter {
                        eq("id_user", userId)
                    }
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Donasi>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyClaims(): Result<List<Claim>> {
        return try {
            val userId = client.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("User tidak ditemukan"))

            // Dalam implementasi nyata, kita mungkin butuh join dengan tabel donasi
            // Tapi untuk sekarang kita ambil data klaim dasarnya dulu
            val result = client.from("klaim")
                .select(
                    Columns.raw("""
                     *,
                     donasi(*)
                """.trimIndent())
                ) {
                    filter {
                        eq("id_user", userId)
                    }
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Claim>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmReceipt(idKlaim: String, idDonasi: String, rating: Double): Result<String> {
        return try {
            // 1. Ambil data donasi untuk mendapatkan id_user donor
            val donasiResult = client.from("donasi")
                .select(Columns.ALL) {
                    filter {
                        eq("id_donasi", idDonasi)
                    }
                }
                .decodeList<Donasi>()

            if (donasiResult.isEmpty()) {
                return Result.failure(Exception("Donasi tidak ditemukan"))
            }
            val donorId = donasiResult.first().id_user
                ?: return Result.failure(Exception("Donor tidak ditemukan"))

            // 2. Update status di tabel klaim jadi 'Diterima'
            client.from("klaim").update(
                {
                    Claim::status setTo "Diterima"
                }
            ) {
                filter {
                    eq("id_klaim", idKlaim)
                }
            }

            // 3. Update status di tabel donasi jadi 'Selesai' dan simpan rating
            client.from("donasi").update(
                {
                    Donasi::status setTo "Selesai"
                    Donasi::rating setTo rating
                }
            ) {
                filter {
                    eq("id_donasi", idDonasi)
                }
            }

            // 4. Hitung rata-rata rating baru untuk donor ini
            val allDonationsWithRating = client.from("donasi")
                .select(Columns.ALL) {
                    filter {
                        eq("id_user", donorId)
                    }
                }
                .decodeList<Donasi>()

            val ratedDonations = allDonationsWithRating.mapNotNull { it.rating }
            val averageRating = if (ratedDonations.isNotEmpty()) {
                ratedDonations.average()
            } else {
                rating
            }

            // 5. Update rating di tabel users
            client.from("users").update(
                {
                    User::rating setTo averageRating
                }
            ) {
                filter {
                    eq("id_user", donorId)
                }
            }

            Result.success("Klaim berhasil diselesaikan")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
