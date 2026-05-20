package com.example.bagiin.repository

import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.User
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class ProfileRepository {

    private val client = SupabaseInstance.client

    suspend fun getProfile(): Result<User> {
        return try {
            val userEmail = client.auth.currentUserOrNull()?.email
                ?: return Result.failure(Exception("User tidak ditemukan"))

            val result = client.from("users")
                .select(Columns.ALL) {
                    filter {
                        eq("email", userEmail)
                    }
                }
                .decodeList<User>()

            if (result.isEmpty()) {
                Result.failure(Exception("Data user tidak ditemukan"))
            } else {
                Result.success(result.first())
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(nama: String, noHp: String, alamat: String): Result<String> {
        return try {
            val userEmail = client.auth.currentUserOrNull()?.email
                ?: return Result.failure(Exception("User tidak ditemukan"))

            client.from("users").update(
                {
                    User::nama setTo nama
                    User::no_hp setTo noHp
                    User::alamat setTo alamat
                }
            ) {
                filter {
                    eq("email", userEmail)
                }
            }

            Result.success("Profil berhasil diupdate")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}