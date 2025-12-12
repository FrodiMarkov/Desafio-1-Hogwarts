package com.example.desafio1.ViewModel

import Api.retrofit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.UsuarioConRoles
import kotlinx.coroutines.launch
import model.Asignatura

class LoginViewModel() : ViewModel() {

    private val usuariosService = retrofit.usuariosRetrofit
    private val asignaturasService = retrofit.asignaturasRetrofit

    val usuarioLogueado = MutableLiveData<UsuarioConRoles?>()

    // LiveData para manejar errores
    val error = MutableLiveData<String?>()

    val listaAsignaturas = MutableLiveData<List<Asignatura>?>()

    val asignaturaAsignada = MutableLiveData<Asignatura?>()


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val usuarios = usuariosService.listarUsuariosConRoles()
                val usuario = usuarios.find { it.nombre == email && it.contrasena == password }

                if (usuario != null) {
                    usuarioLogueado.value = usuario
                    error.value = null

                    if (usuario.roles.contains(2)) {
                        cargarDatosDeProfesor(usuario.id)
                    }

                } else {
                    usuarioLogueado.value = null
                    error.value = "Email o contraseña incorrectos"
                }
            } catch (e: Exception) {
                usuarioLogueado.value = null
                error.value = "Error en la conexión: ${e.message}"
            }
        }
    }

    private fun cargarDatosDeProfesor(profesorId: Int) {
        viewModelScope.launch {
            try {
                val asignaturas = asignaturasService.listarAsignaturas()
                listaAsignaturas.value = asignaturas

                val asignaturaUnica = asignaturas.find { it.id_profesor == profesorId }
                asignaturaAsignada.value = asignaturaUnica

            } catch (e: Exception) {
                error.value = "Error al cargar las asignaturas: ${e.message}"
                listaAsignaturas.value = emptyList()
                asignaturaAsignada.value = null
            }
        }
    }
}