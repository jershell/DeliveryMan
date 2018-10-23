package tech.cadia

import kotlinx.serialization.*

@Serializable
data class Result(val result: Boolean)

@Serializable
data class Message(val message: String)

@Serializable
data class Error(val message: String)

@Serializable
data class FormFields(
        @Optional val name: String = "",
        @Optional val phone: String = "",
        @Optional val email: String = "",
        @Optional val message: String = "") {
    fun toMap(): Map<String, String> {
        return mapOf("name" to name, "phone" to phone, "email" to email, "message" to message )
    }
}