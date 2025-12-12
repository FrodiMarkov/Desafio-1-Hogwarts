package model

import kotlinx.serialization.Serializable

@Serializable
data class AlumnoAsignatura(
    val id_asignatura: Int,
    val id_alumno: Int
)