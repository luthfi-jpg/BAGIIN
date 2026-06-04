package com.example.bagiin.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import com.example.bagiin.BuildConfig

object SupabaseInstance {

    val client = createSupabaseClient(
        supabaseUrl = "https://goonlhhgstkqxmhaoqbc.supabase.co",
        supabaseKey = "sb_publishable_SMsxzIylSQ62UmiYQPcUaA_HtfHPzgo"
    ) {
        install(Auth)
        install(Postgrest)
    }
}