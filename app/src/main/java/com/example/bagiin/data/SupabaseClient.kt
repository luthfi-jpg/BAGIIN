package com.example.bagiin.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import com.example.bagiin.BuildConfig

object SupabaseInstance {

    val client = createSupabaseClient(
        supabaseUrl = "https://zhvxtfhqstnldfizejum.supabase.co",
        supabaseKey = "sb_publishable_0aP1JbRcVh1EYSsRhDrlwA_0iwFxLrS"
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}