package com.example.desafio1.model

import kotlinx.serialization.Serializable

@Serializable
data class CreacionAsignatura(
     val nombre: String,
     val idProfesor: Int,
     val idsAlumnos: List<Int>
)