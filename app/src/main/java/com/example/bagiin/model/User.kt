package com.example.bagiin.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id_user: String? = null,
    val nama: String = "",
    val email: String = "",
    val no_hp: String? = null,
    val alamat: String? = null,
    val foto_profil: String? = null
)