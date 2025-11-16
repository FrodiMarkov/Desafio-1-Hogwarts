package com.example.desafio1.ViewModel

import Api.EncuestaNetwork.retrofit
import android.util.Log
import androidx.lifecycle.*
import com.example.desafio1.model.Usuario
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val _registroResult = MutableLiveData<Boolean>()
    val registroResult: LiveData<Boolean> get() = _registroResult

    fun registrarUsuario(
        nombre: String,
        email: String,
        contrasena: String,
        prioridadG: Int,
        prioridadS: Int,
        prioridadR: Int,
        prioridadH: Int
    ) {
        if (nombre.isBlank() || email.isBlank() || contrasena.isBlank()) {
            _registroResult.value = false
            return
        }

        val prioridades = listOf(prioridadG, prioridadS, prioridadR, prioridadH)
        val max = prioridades.maxOrNull() ?: 1
        val idCasa = when (max) {
            prioridadG -> 1
            prioridadS -> 2
            prioridadR -> 3
            else -> 4
        }

        val usuario = Usuario(
            nombre = nombre,
            email = email,
            contrasena = contrasena,
            idCasa = idCasa
        )

        viewModelScope.launch {
            try {
                val response = retrofit.insertarUsuario(usuario)
                Log.d("RegistroViewModel", "HTTP code: ${response.code()}")

                _registroResult.postValue(response.isSuccessful)
            } catch (e: Exception) {
                Log.e("RegistroViewModel", "Error al insertar usuario", e)
                _registroResult.postValue(false)
            }
        }
    }
}
