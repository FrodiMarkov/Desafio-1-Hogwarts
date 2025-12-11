package com.example.desafio1.API

import com.example.desafio1.model.Hechizo
import retrofit2.http.Body
import retrofit2.http.POST

interface hechizosAPI {
    @POST("/hechizos") // Debe coincidir con la ruta definida en Ktor
    suspend fun crearHechizo(@Body hechizo: Hechizo): Hechizo
}