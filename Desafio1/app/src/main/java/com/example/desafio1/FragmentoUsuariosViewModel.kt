package com.example.desafio1

import Api.HowartsNetwork.retrofit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio1.API.usuariosAPI
import com.example.desafio1.model.Registro
import com.example.model.UsuarioConRoles
import kotlinx.coroutines.launch
import retrofit2.Response

class FragmentoUsuariosViewModel : ViewModel() {
    private val _usuarios = MutableLiveData<List<UsuarioConRoles>>(emptyList())
    val usuarios: LiveData<List<UsuarioConRoles>> get() = _usuarios

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> get() = _mensajeError

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                val lista = retrofit.listarUsuariosConRoles()
                _usuarios.value = lista
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar usuarios: ${e.message}"
            }
        }
    }

    fun crearUsuario(registro: Registro) {
        viewModelScope.launch {
            try {
                val response: Response<Registro> = retrofit.registrarUsuario(registro)
                if (response.isSuccessful) {
                    cargarUsuarios()
                } else {
                    _mensajeError.value = "Error al crear usuario: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al crear usuario: ${e.message}"
            }
        }
    }

    fun editarUsuario(usuario: UsuarioConRoles) {
        viewModelScope.launch {
            try {
                val response = retrofit.modificarUsuario(usuario.id, usuario)
                if (response.isSuccessful) {
                    cargarUsuarios()
                    _mensajeError.value = "Se ha modificado el usuario con éxito"
                } else {
                    _mensajeError.value = "Error al editar usuario: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al editar usuario: ${e.message}"
            }
        }
    }

    fun eliminarUsuario(id: Int) {
        viewModelScope.launch {
            try {
                val exito: Boolean = retrofit.eliminarUsuario(id)
                if (exito) {
                    cargarUsuarios()
                } else {
                    _mensajeError.value = "Error al eliminar usuario"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al eliminar usuario: ${e.message}"
            }
        }
    }

}