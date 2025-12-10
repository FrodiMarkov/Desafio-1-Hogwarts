package DAO

import model.Asignatura
import model.Usuario
import model.UsuarioConRoles

interface DumbledorDAO {
    fun insertar(usuario: Usuario): Int?
    fun modificar(usuario: UsuarioConRoles): Boolean
    fun eliminar(id: Int?): Boolean
    fun listarUsuariosConRoles(): List<UsuarioConRoles>
    fun asignarRol(usuarioId: Int?, rolId: Int?) : Boolean
    fun seleccionarCasaEquilibrada(preferencias: Map<Int, Int>): Int?
    fun todasAsignaturas(): List<Asignatura>
    fun asignaturaById(id: Int): Asignatura?
    fun crearAsignatura(nombre: String): Int?
    fun modificarAsignatura(id: Int, nuevoNombre: String, nuevoIdProfesor: Int): Boolean
    fun borrarAsignatura(id: Int): Boolean
    fun crearAsignaturaCompleta(nombre: String, idProfesor: Int, idsAlumnos: List<Int>): Boolean
}