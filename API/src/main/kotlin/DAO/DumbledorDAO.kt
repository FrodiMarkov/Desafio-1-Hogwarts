package DAO

import model.Usuario
import model.UsuarioConRoles

interface DumbledorDAO {
    fun insertar(usuario: Usuario): Int?
    fun modificar(usuario: UsuarioConRoles): Boolean
    fun eliminar(id: Int?): Boolean
    fun listarUsuariosConRoles(): List<UsuarioConRoles>
    fun asignarRol(usuarioId: Int?, rolId: Int?) : Boolean
    fun seleccionarCasaEquilibrada(preferencias: Map<Int, Int>): Int?
}