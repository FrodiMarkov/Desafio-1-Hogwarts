package com.example.desafio1.API

import com.example.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface usuariosAPI {
    @GET("usuarios")
    suspend fun listarUsuarios(): List<Usuario>

    @POST("usuarios")
    suspend fun insertarUsuario(@Body usuario: Usuario): Boolean

    @PUT("usuarios/{id}")
    suspend fun modificarUsuario(@Path("id") id: Int, @Body usuario: Usuario): Boolean

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Boolean
}