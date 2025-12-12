package model

import kotlinx.serialization.Serializable

@Serializable
data class Casa (
    val id_casa: Int,
    val nombreCasa: String,
    val puntosCasa: Int
)