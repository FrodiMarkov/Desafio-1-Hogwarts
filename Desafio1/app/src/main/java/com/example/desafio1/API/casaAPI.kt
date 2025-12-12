package com.example.desafio1.API

import model.Casa
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface casaAPI {
    @GET("casas")
    suspend fun getCasas(): Response<List<Casa>>

    @PUT("casas")
    suspend fun modificarCasa(@Body casa: Casa): Response<String>
}