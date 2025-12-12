package com.example.desafio1.viewmodels // O donde tengas tus ViewModels

import Api.retrofit.asignaturasRetrofit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.Asignatura
import retrofit2.HttpException
import java.io.IOException

class ElegirRolViewModel(

) : ViewModel() {

    private val _asignaturasDelProfesor = MutableLiveData<List<Asignatura>>()
    val asignaturasDelProfesor: LiveData<List<Asignatura>> = _asignaturasDelProfesor

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarAsignaturas(profesorId: Int) {
        viewModelScope.launch {
            _error.value = null
            try {
                val listaCompleta = asignaturasRetrofit.listarAsignaturas()

                val asignaturasFiltradas = listaCompleta.filter { it.id_profesor == profesorId }

                _asignaturasDelProfesor.value = asignaturasFiltradas

            } catch (e: HttpException) {
                _error.value = "Error de servidor: ${e.code()}"
                _asignaturasDelProfesor.value = emptyList()
            } catch (e: IOException) {
                _error.value = "Error de conexión: Verifica tu red."
                _asignaturasDelProfesor.value = emptyList()
            } catch (e: Exception) {
                _error.value = "Error inesperado: ${e.message}"
                _asignaturasDelProfesor.value = emptyList()
            }
        }
    }
}