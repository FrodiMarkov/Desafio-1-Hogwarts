package com.example.desafio1.API

import com.example.desafio1.model.Registro
import com.example.model.UsuarioConRoles
import retrofit2.Response
import retrofit2.http.*

interface usuariosAPI {
    @GET("usuario")
    suspend fun listarUsuariosConRoles(): List<UsuarioConRoles>

    @POST("usuario/registrar")
    suspend fun registrarUsuario(@Body request: Registro): Response<Registro>

    @POST("usuario/registrarConRoles")
    suspend fun registrarUsuarioConRoles(@Body request: UsuarioConRoles): Response<UsuarioConRoles>

    @PUT("usuario/{id}")
    suspend fun modificarUsuario(@Path("id") id: Int, @Body usuario: UsuarioConRoles): Response<Unit>

    @DELETE("usuario/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Boolean


}