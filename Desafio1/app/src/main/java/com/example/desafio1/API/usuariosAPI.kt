package com.example.desafio1.API

import com.example.model.UsuarioConRoles
import retrofit2.Call
import retrofit2.http.*

interface usuariosAPI {
    @GET("usuario")
    suspend fun listarUsuariosConRoles(): List<UsuarioConRoles>

    @POST("usuario")
    suspend fun insertarUsuario(@Body usuario: UsuarioConRoles): Boolean

    @PUT("usuario/{id}")
    suspend fun modificarUsuario(@Path("id") id: Int, @Body usuario: UsuarioConRoles): Boolean

    @DELETE("usuario/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Boolean


}