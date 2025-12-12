package com.example.desafio1.ViewModel

import Api.retrofit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.Casa

class RankingViewModel () : ViewModel() {
    private val _casasRanking = MutableLiveData<List<Casa>>()
    val casasRanking: LiveData<List<Casa>> = _casasRanking


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        cargarRanking()
    }

    fun cargarRanking() {
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = retrofit.casaRetrofit.getCasas()

                if (response.isSuccessful && response.body() != null) {
                    val ranking = response.body()!!.sortedByDescending { it.puntosCasa }
                    _casasRanking.value = ranking
                } else {
                    _casasRanking.value = emptyList()
                    _errorMessage.value = "Error HTTP: Código ${response.code()}"
                }
            } catch (e: Exception) {
                _casasRanking.value = emptyList()
                _errorMessage.value = "Error de conexión/parsing: ${e.message}"
            }
        }
    }
}