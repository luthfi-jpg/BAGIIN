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
        password: String
    ): Result<String> {

        return try {

            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val user = User(
                nama = nama,
                email = email
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

            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            Result.success("Login berhasil")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}