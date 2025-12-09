package com.example.desafio1.ViewModel

import Api.HowartsNetwork.usuariosRetrofit
import androidx.lifecycle.*
import com.example.desafio1.model.Registro
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class RegistroViewModel : ViewModel() {

    private val _registroResult = MutableLiveData<Boolean>()
    val registroResult: LiveData<Boolean> get() = _registroResult

    fun registrarUsuario(
        nombre: String,
        email: String,
        contrasena: String,
        g: Int,
        s: Int,
        r: Int,
        h: Int
    ) {
        viewModelScope.launch {
            try {
                val request = Registro(nombre, email, contrasena, g, s, r, h)
                val response = usuariosRetrofit.registrarUsuario(request)
                _registroResult.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _registroResult.postValue(false)
            }
        }
    }
}
