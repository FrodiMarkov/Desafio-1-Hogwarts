package com.example.desafio1.ViewModel

import Api.retrofit
import Api.retrofit.casaRetrofit
import Api.retrofit.usuariosRetrofit // Asumimos que esta es la ruta a tu API de usuarios
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio1.model.Hechizo
import com.example.model.UsuarioConRoles
import com.example.desafio1.Holders.UsuarioHolder
import kotlinx.coroutines.launch

class FragmentoHechizosAlumnoViewModel : ViewModel() {


    private val _hechizos = MutableLiveData<List<Hechizo>>()
    val hechizos: LiveData<List<Hechizo>> = _hechizos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Nuevo LiveData para notificar el resultado de la actualización de EXP
    private val _usuarioUpdateResult = MutableLiveData<String>()
    val usuarioUpdateResult: LiveData<String> = _usuarioUpdateResult

    init {
        cargarHechizos()
    }

    fun cargarHechizos() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val listaHechizos = retrofit.hechizosRetrofit.obtenerTodosHechizos()
                _hechizos.value = listaHechizos
            } catch (e: Exception) {
                _error.value = "Error al cargar hechizos: ${e.localizedMessage}"
                _hechizos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Lógica para calcular la nueva experiencia y actualizar el usuario en el servidor.
     */
    fun aprenderHechizo(hechizo: Hechizo, usuarioActual: UsuarioConRoles) {
        // Limpiar resultado anterior
        _usuarioUpdateResult.value = ""

        viewModelScope.launch {
            try {
                val expGanada = hechizo.experiencia
                val nuevaExperiencia = usuarioActual.experiencia + expGanada
                val nuevoNivel = when (nuevaExperiencia) {
                    in 51..150 -> 2
                    in 151..300 -> 3
                    in 301..500 -> 4
                    in 501..Int.MAX_VALUE -> 5
                    else -> 1
                }


                // Crear una copia del usuario con la nueva experiencia
                val usuarioActualizado = usuarioActual.copy(experiencia = nuevaExperiencia, nivel = nuevoNivel)

                // Llamada a la API usando la función suspend de Retrofit
                val exito = usuariosRetrofit.modificarUsuario(usuarioActualizado.id, usuarioActualizado)

                if (exito) {
                    // Actualizar el Holder global si la API confirma el éxito
                    UsuarioHolder.usuario = usuarioActualizado
                    _usuarioUpdateResult.value = "¡Hechizo ejecutado! Ganaste $expGanada EXP (Total: $nuevaExperiencia)."
                } else {
                    _usuarioUpdateResult.value = "Error: La API no pudo actualizar al usuario."
                }
            } catch (e: Exception) {
                _usuarioUpdateResult.value = "Error de conexión o servidor al actualizar EXP: ${e.message}"
            }
        }
    }
}