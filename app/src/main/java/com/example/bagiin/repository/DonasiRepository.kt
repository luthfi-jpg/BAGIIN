package com.example.bagiin.repository

import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.Claim
import com.example.bagiin.model.Donasi
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage

class DonasiRepository {
    private val client = SupabaseInstance.client

    suspend fun getDonasi(): Result<List<Donasi>> {
        return try {
            val result = client.from("donasi")
                .select(Columns.raw("*, donor:users(nama, foto_profil, rating)")) {
                    filter {
                        or {
                            eq("status", "tersedia")
                            eq("status", "Tersedia")
                        }
                    }
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Donasi>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDonasi(idDonasi: String): Result<String> {
        return try {
            client.from("donasi").delete {
                filter {
                    eq("id_donasi", idDonasi)
                }
            }
            Result.success("Donasi berhasil dihapus")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun insertDonasi(donasi: Donasi): Result<String> {
        return try {
            client.from("donasi").insert(donasi)
            Result.success("Donasi berhasil ditambahkan")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadDonasiImage(userId: String, byteArray: ByteArray, fileName: String): Result<String> {
        return try {
            val bucket = client.storage.from("foto")
            val path = "$userId/$fileName"
            bucket.upload(path, byteArray) {
                upsert = true
            }
            val url = bucket.publicUrl(path)
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadMultipleImages(userId: String, images: List<ByteArray>): Result<List<String>> {
        return try {
            val urls = mutableListOf<String>()
            images.forEachIndexed { index, bytes ->
                val fileName = "donation_${System.currentTimeMillis()}_$index.jpg"
                val result = uploadDonasiImage(userId, bytes, fileName)
                urls.add(result.getOrThrow())
            }
            Result.success(urls)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getDonasiById(id: String): Result<Donasi> {
        return try {
            val result = client.from("donasi")
                .select(Columns.raw("*, donor:users(nama, foto_profil, rating)")) {
                    filter {
                        eq("id_donasi", id)
                    }
                }
                .decodeSingle<Donasi>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDonationCountByUser(userId: String): Result<Long> {
        return try {
            val response = client.from("donasi").select(columns = Columns.raw("id_donasi")) {
                filter {
                    eq("id_user", userId)
                }
            }
            val count = response.decodeList<Donasi>().size.toLong()
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun claimDonasi(idDonasi: String, alasan: String): Result<String> {
        return try {
            val userId = client.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("User tidak ditemukan"))

            val claim = Claim(
                id_donasi = idDonasi,
                id_user = userId,
                alasan = alasan,
                status = "Pending"
            )

            // 1. Insert claim row
            client.from("klaim").insert(claim)
            
            // 2. Update status of the donation to 'diklaim'
            client.from("donasi").update(
                {
                    Donasi::status setTo "diklaim"
                }
            ) {
                filter {
                    eq("id_donasi", idDonasi)
                }
            }

            Result.success("Klaim berhasil diajukan")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
