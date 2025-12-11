package ViewModel

import Api.retrofit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.UsuarioConRoles
import kotlinx.coroutines.launch
import model.Asignatura

class LoginViewModel() : ViewModel() {

    // Servicios Retrofit
    private val usuariosService = retrofit.usuariosRetrofit
    private val asignaturasService = retrofit.asignaturasRetrofit // <-- Nuevo servicio

    // LiveData para el usuario autenticado
    val usuarioLogueado = MutableLiveData<UsuarioConRoles?>()

    // LiveData para manejar errores
    val error = MutableLiveData<String?>()

    // LiveData para la lista completa de asignaturas (necesaria para el mapeo de IDs)
    val listaAsignaturas = MutableLiveData<List<Asignatura>?>()

    // LiveData para la asignatura específica asignada al profesor (si solo imparte una)
    val asignaturaAsignada = MutableLiveData<Asignatura?>()


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                // 1. Autenticación de Usuario
                val usuarios = usuariosService.listarUsuariosConRoles()
                val usuario = usuarios.find { it.nombre == email && it.contrasena == password }

                if (usuario != null) {
                    usuarioLogueado.value = usuario
                    error.value = null

                    // 2. Si el usuario es Profesor (Rol 2), cargar datos adicionales
                    if (usuario.roles.contains(2)) {
                        cargarDatosDeProfesor(usuario.id)
                    }

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

    /**
     * Carga todas las asignaturas y determina la que imparte el profesor (asumiendo una sola).
     */
    private fun cargarDatosDeProfesor(profesorId: Int) {
        viewModelScope.launch {
            try {
                val asignaturas = asignaturasService.listarAsignaturas() // Asume que esto trae todas las asignaturas
                listaAsignaturas.value = asignaturas

                // Intentar encontrar la única asignatura asignada al profesor
                val asignaturaUnica = asignaturas.find { it.id_profesor == profesorId }
                asignaturaAsignada.value = asignaturaUnica

            } catch (e: Exception) {
                error.value = "Error al cargar las asignaturas: ${e.message}"
                listaAsignaturas.value = emptyList()
                asignaturaAsignada.value = null
            }
        }
    }
}