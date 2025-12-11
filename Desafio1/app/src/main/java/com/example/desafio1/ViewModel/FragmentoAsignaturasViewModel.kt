package com.example.desafio1.ViewModel

import Api.retrofit // Asumiendo que HowartsNetwork es donde se define Retrofit
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.UsuarioConRoles
import kotlinx.coroutines.launch
import model.Asignatura

class FragmentoAsignaturasViewModel : ViewModel() {

    private val _asignaturas = MutableLiveData<List<Asignatura>>(emptyList())
    val asignaturas: LiveData<List<Asignatura>> get() = _asignaturas

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> get() = _mensajeError

    private val ROL_PROFESOR = 3 // Define la constante para el rol de profesor

    private val _profesores = MutableLiveData<List<UsuarioConRoles>>(emptyList())
    val profesores: LiveData<List<UsuarioConRoles>> get() = _profesores

    fun cargarProfesores() {
        viewModelScope.launch {
            try {
                val todosUsuarios = retrofit.usuariosRetrofit.listarUsuariosConRoles()

                val listaProfesores = todosUsuarios.filter {
                    it.roles.contains(ROL_PROFESOR)
                }

                _profesores.value = listaProfesores
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar profesores: ${e.message}"
            }
        }
    }
    fun cargarAsignaturas() {
        viewModelScope.launch {
            try {
                val lista = retrofit.asignaturasRetrofit.listarAsignaturas()
                _asignaturas.value = lista
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar asignaturas: ${e.message}"
            }
        }
    }

    fun editarAsignatura(asignatura: Asignatura) {

        if (asignatura.id <= 0) {
            val mensaje = "Error: La asignatura no tiene un ID válido (${asignatura.id}) para editar."
            Log.e("EDIT_ASIGNATURA", mensaje)
            _mensajeError.value = mensaje
            return
        }

        Log.d("EDIT_ASIGNATURA", "Intentando PUT /asignatura/${asignatura.id}")
        Log.d("EDIT_ASIGNATURA", "Datos enviados: Nombre='${asignatura.nombre}', idProfesor='${asignatura.id_profesor}'")

        viewModelScope.launch {
            try {
                val response = retrofit.asignaturasRetrofit.modificarAsignatura(asignatura.id, asignatura)

                if (response.isSuccessful) {
                    Log.i("EDIT_ASIGNATURA", "Modificación exitosa. Recargando lista.")
                    cargarAsignaturas() // Recargar lista al tener éxito
                } else {
                    val errorBody = response.errorBody()?.string() ?: "N/A"
                    val mensaje = "Error al editar asignatura: Código ${response.code()}. Cuerpo de error: $errorBody"
                    Log.e("EDIT_ASIGNATURA", mensaje)
                    _mensajeError.value = mensaje
                }
            } catch (e: Exception) {
                val mensaje = "Error al editar asignatura: ${e.message}"
                Log.e("EDIT_ASIGNATURA", mensaje, e)
                _mensajeError.value = mensaje
            }
        }
    }

    fun eliminarAsignatura(id: Int) {
        viewModelScope.launch {
            try {
                // Asumiendo que HowartsNetwork.retrofit tiene un método eliminarAsignatura(id)
                val exito = retrofit.asignaturasRetrofit.eliminarAsignatura(id)
                if (exito) {
                    cargarAsignaturas() // Recargar lista al tener éxito
                } else {
                    _mensajeError.value = "Error al eliminar asignatura"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al eliminar asignatura: ${e.message}"
            }
        }
    }
}