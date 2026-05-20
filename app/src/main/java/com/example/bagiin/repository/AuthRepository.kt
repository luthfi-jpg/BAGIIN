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

            val authResult = client.auth.signUpWith(Email) {
                this.email = cleanEmail
                this.password = cleanPassword
            }

            val userId = client.auth.currentUserOrNull()?.id ?: ""

            val user = User(
                id_user = userId,
                nama = nama,
                email = cleanEmail,
                no_hp = noHp
            )

            client.from("users").insert(user)

            Result.success("Register berhasil")

        } catch (e: Exception) {
            Result.failure(e)
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
            Result.failure(e)
        }
    }
}