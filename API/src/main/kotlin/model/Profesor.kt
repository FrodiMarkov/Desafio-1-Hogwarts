package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Profesor(
    var id: Int,
    var Nombre: String,
    var Rol: Int,
    var Clase: String
)