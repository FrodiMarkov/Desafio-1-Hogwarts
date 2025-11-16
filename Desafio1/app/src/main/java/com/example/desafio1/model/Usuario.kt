package com.example.model


data class Usuario(
    val id: Int? = null,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val experiencia: Int,
    val id_casa: Int,
    val nivel: Int
)