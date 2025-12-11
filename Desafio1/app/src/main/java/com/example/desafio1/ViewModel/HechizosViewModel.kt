package com.example.desafio1.ViewModel

import Api.retrofit.hechizosRetrofit
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio1.model.Hechizo
import kotlinx.coroutines.launch

class HechizosViewModel : ViewModel() {

    val guardarExitoso = MutableLiveData<Boolean>()
    val errorMensaje = MutableLiveData<String?>()

    fun validarYGuardarHechizo(
        nombre: String,
        descripcion: String,
        experienciaStr: String
    ) {
        errorMensaje.value = null
        guardarExitoso.value = false

        if (nombre.trim().isEmpty() || descripcion.trim().isEmpty() || experienciaStr.trim().isEmpty()) {
            errorMensaje.value = "Por favor, completa todos los campos y selecciona un icono."
            return
        }

        val experiencia = experienciaStr.toIntOrNull()
        if (experiencia == null || experiencia < 1) {
            errorMensaje.value = "Experiencia debe ser un número entero positivo."
            return
        }

        iniciarGuardadoEnAPI(nombre, descripcion, experiencia)
    }


    private fun iniciarGuardadoEnAPI(
        nombre: String,
        descripcion: String,
        experiencia: Int
    ) {
        viewModelScope.launch {
            try {
                val urlPublica = "pruebaImagen"

                val nuevoHechizo = Hechizo(
                    id = 0, // El ID se asignará en el servidor (Ktor)
                    nombre = nombre,
                    descripcion = descripcion,
                    experiencia = experiencia,
                    urlIcono = urlPublica // Usamos la URL que obtuvimos
                )
                val hechizoGuardado = hechizosRetrofit.crearHechizo(nuevoHechizo)

                if (hechizoGuardado.id > 0) {
                    guardarExitoso.value = true
                } else {
                    errorMensaje.value = "El servidor no pudo confirmar el guardado."
                }

            } catch (e: Exception) {
                errorMensaje.value = "Error de conexión o servidor: ${e.message}"
                guardarExitoso.value = false
            }
        }
    }
}