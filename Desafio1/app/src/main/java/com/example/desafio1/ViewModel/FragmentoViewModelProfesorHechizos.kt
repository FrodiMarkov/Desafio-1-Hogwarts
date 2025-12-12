package com.example.desafio1.ViewModel

import Api.retrofit.hechizosRetrofit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio1.model.Hechizo
import kotlinx.coroutines.launch

// Importa tu RetrofitClient
// Ejemplo: import com.example.desafio1.API.RetrofitClient

class FragmentoViewModelProfesorHechizos : ViewModel() {


    private val _hechizos = MutableLiveData<List<Hechizo>>()
    val hechizos: LiveData<List<Hechizo>> = _hechizos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        cargarHechizos()
    }

    fun cargarHechizos() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val listaHechizos = hechizosRetrofit.obtenerTodosHechizos()
                _hechizos.value = listaHechizos
            } catch (e: Exception) {
                _error.value = "Error al cargar hechizos: ${e.localizedMessage}"
                _hechizos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}