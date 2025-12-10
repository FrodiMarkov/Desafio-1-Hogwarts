package model

import kotlinx.serialization.Serializable

@Serializable
data class Asignatura(
    val id: Int,
    val nombre: String,
    val id_profesor: Int
)