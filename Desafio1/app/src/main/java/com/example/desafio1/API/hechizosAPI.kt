package com.example.desafio1.API

import com.example.desafio1.model.Hechizo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface hechizosAPI {
    @POST("/hechizos")
    suspend fun crearHechizo(@Body hechizo: Hechizo): Hechizo
    @GET("/hechizos")
    suspend fun obtenerTodosHechizos(): List<Hechizo>
}