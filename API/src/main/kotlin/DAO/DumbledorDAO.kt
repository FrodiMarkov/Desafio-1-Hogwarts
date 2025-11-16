package com.example.DAO

import com.example.model.Usuario

interface DumbledorDAO {
    fun insertar(usuario: Usuario): Boolean
    fun modificar(usuario: Usuario): Boolean
    fun eliminar(id: Int?): Boolean
    fun listar(): List<Usuario>
    fun listarUsuariosConRoles(): List<UsuarioConRoles>
}