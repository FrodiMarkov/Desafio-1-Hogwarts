package DAO

import model.UsuarioConRoles

interface DumbledorDAO {
    fun insertar(usuario: UsuarioConRoles): Boolean
    fun modificar(usuario: UsuarioConRoles): Boolean
    fun eliminar(id: Int?): Boolean
    fun listarUsuariosConRoles(): List<UsuarioConRoles>
}