package ViewModel

import Api.HowartsNetwork
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.UsuarioConRoles
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    private val retrofit = HowartsNetwork.usuariosRetrofit
    val usuarioLogueado = MutableLiveData<UsuarioConRoles?>()
    val error = MutableLiveData<String?>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val usuarios = retrofit.listarUsuariosConRoles()
                val usuario = usuarios.find { it.nombre == email && it.contrasena == password }

                if (usuario != null) {
                    usuarioLogueado.value = usuario
                    error.value = null
                } else {
                    usuarioLogueado.value = null
                    error.value = "Email o contraseña incorrectos"
                }
            } catch (e: Exception) {
                usuarioLogueado.value = null
                error.value = "Error en la conexión: ${e.message}"
            }
        }
    }
}