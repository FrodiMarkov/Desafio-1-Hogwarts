package model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val nombre: String,
    val email: String,
    val contrasena: String,
    val experiencia: Int = 0,
    val nivel: Int = 0,
    val idCasa: Int
)