package com.example.desafio1.ViewModel

import Api.HowartsNetwork
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.UsuarioConRoles
import kotlinx.coroutines.launch

class FragmentoUsuariosViewModel : ViewModel() {

    private val _usuarios = MutableLiveData<List<UsuarioConRoles>>(emptyList())
    val usuarios: LiveData<List<UsuarioConRoles>> get() = _usuarios

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> get() = _mensajeError

    // Cargar lista desde API
    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                val lista = HowartsNetwork.usuariosRetrofit.listarUsuariosConRoles()
                _usuarios.value = lista
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar usuarios: ${e.message}"
            }
        }
    }

    // Editar usuario
    fun editarUsuario(usuario: UsuarioConRoles) {
        viewModelScope.launch {
            try {
                val response = HowartsNetwork.usuariosRetrofit.modificarUsuario(usuario.id, usuario)
                if (response.isSuccessful) {
                    cargarUsuarios()
                } else {
                    _mensajeError.value = "Error al editar usuario: Código ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al editar usuario: ${e.message}"
            }
        }
    }

    // Eliminar usuario
    fun eliminarUsuario(id: Int) {
        viewModelScope.launch {
            try {
                val exito = HowartsNetwork.usuariosRetrofit.eliminarUsuario(id)
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