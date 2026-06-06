package com.example.bagiin.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

object StringListSerializer : KSerializer<List<String>> {
    private val delegate = ListSerializer(String.serializer())
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: List<String>) {
        encoder.encodeSerializableValue(delegate, value)
    }

    override fun deserialize(decoder: Decoder): List<String> {
        val jsonDecoder = decoder as? JsonDecoder
        if (jsonDecoder != null) {
            val element = jsonDecoder.decodeJsonElement()
            if (element is JsonArray) {
                return element.map { it.jsonPrimitive.content }
            } else if (element is JsonPrimitive) {
                val content = element.content
                if (content.startsWith("[") && content.endsWith("]")) {
                    try {
                        val parsed = jsonDecoder.json.parseToJsonElement(content)
                        if (parsed is JsonArray) {
                            return parsed.map { it.jsonPrimitive.content }
                        }
                    } catch (e: Exception) {
                        // fallback
                    }
                }
                if (content.isEmpty()) return emptyList()
                return listOf(content)
            }
        }
        return decoder.decodeSerializableValue(delegate)
    }
}

@Serializable
data class Donasi(
    val id_donasi: String? = null,
    val id_user: String? = null,
    val judul: String? = null,
    val deskripsi: String? = null,
    val kategori: String? = null,
    val kondisi: String? = null,
    @Serializable(with = StringListSerializer::class)
    val foto_url: List<String>? = null,
    val status: String? = "tersedia",
    val rating: Double? = null,
    val created_at: String? = null,
    val lokasi: String? = null,
    val alasan: String? = null,
    val avatar_url: String? = null,
    val donor: Donor? = null
)

@Serializable
data class Donor(
    val nama: String? = null,
    val foto_profil: String? = null
)
