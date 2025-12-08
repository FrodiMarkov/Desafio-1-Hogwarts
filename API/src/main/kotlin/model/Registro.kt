package model

import kotlinx.serialization.Serializable

@Serializable
data class Registro(
    val nombre: String,
    val email: String,
    val contraseña: String,
    val prefGry: Int,
    val prefSly: Int,
    val prefRav: Int,
    val prefHuf: Int
)