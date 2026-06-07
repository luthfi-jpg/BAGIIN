package com.example.bagiin.repository

import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.User
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class AuthRepository {

    private val client = SupabaseInstance.client

    suspend fun register(
        nama: String,
        email: String,
        password: String,
        noHp: String = ""
    ): Result<String> {
        return try {
            val cleanEmail = email.trim()
            val cleanPassword = password.trim()

            // 1. Sign up ke Supabase Auth
            val response = client.auth.signUpWith(Email) {
                this.email = cleanEmail
                this.password = cleanPassword
            }

            // 2. Ambil ID User yang baru didaftarkan langsung dari response
            val userId = response?.id
                ?: return Result.failure(Exception("Gagal mendaftarkan akun (ID tidak ditemukan)"))

            // 3. Masukkan data ke tabel 'users'
            val user = User(
                id_user = userId,
                nama = nama,
                email = cleanEmail,
                no_hp = noHp
            )
            client.from("users").insert(user)
            Result.success("Register berhasil")

        } catch (e: Exception) {
            e.printStackTrace()
            val errorBody = e.message ?: ""
            val errorMessage = when {
                errorBody.contains("user_already_exists", ignoreCase = true) || 
                errorBody.contains("already registered", ignoreCase = true) -> 
                    "Email sudah terdaftar. Silakan gunakan email lain."
                
                errorBody.contains("network", ignoreCase = true) -> 
                    "Koneksi internet bermasalah. Coba lagi."
                
                else -> "Terjadi kesalahan saat registrasi"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val cleanEmail = email.trim()
            val cleanPassword = password.trim()

            client.auth.signInWith(Email) {
                this.email = cleanEmail
                this.password = cleanPassword
            }

            Result.success("Login berhasil")

        } catch (e: Exception) {
            e.printStackTrace()
            val errorBody = e.message ?: ""
            val errorMessage = when {
                errorBody.contains("invalid login credentials", ignoreCase = true) || 
                errorBody.contains("invalid email or password", ignoreCase = true) -> 
                    "Email atau password salah. Silakan periksa kembali."
                
                errorBody.contains("network", ignoreCase = true) -> 
                    "Koneksi internet bermasalah. Coba lagi."
                
                else -> "Email atau password salah. Silakan periksa kembali."
            }
            Result.failure(Exception(errorMessage))
        }
    }
}