import Api.retrofit.asignaturasRetrofit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio1.Holders.UsuarioHolder
import kotlinx.coroutines.launch
import model.Asignatura // ¡IMPORTANTE! Importar la clase Asignatura

class AlumnoViewModel() : ViewModel() {

    // 1. CORRECCIÓN: Cambiar el tipo de LiveData a List<Asignatura>?
    private val _asignaturas = MutableLiveData<List<Asignatura>?>()
    val asignaturas: LiveData<List<Asignatura>?> = _asignaturas

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Función auxiliar necesaria para que la Activity limpie el error después de mostrarlo
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun cargarAsignaturas() {
        _errorMessage.value = null
        _asignaturas.value = null // Indica que la carga está en curso o pendiente

        viewModelScope.launch {

            // Validación de ID de alumno antes de la llamada (evita NullPointerException)
            val idAlumno = UsuarioHolder.usuario?.id
            if (idAlumno == null) {
                _errorMessage.value = "Error: ID de alumno no disponible. Usuario no logueado."
                return@launch
            }

            try {
                // 1. Obtener IDs de las Asignaturas del alumno
                val responseRelaciones = asignaturasRetrofit.listarAlumnoAsignatura(idAlumno)

                if (!responseRelaciones.isSuccessful) {
                    _errorMessage.value = "Error al cargar relaciones: Código ${responseRelaciones.code()}"
                    return@launch
                }

                val asignaturaIds = responseRelaciones.body()?.map { it.id_asignatura } ?: emptyList()

                if (asignaturaIds.isEmpty()) {
                    _asignaturas.value = emptyList() // Lista vacía = Sin asignaturas
                    return@launch
                }

                // 2. CORRECCIÓN: Obtener los objetos Asignatura completos
                val asignaturasCargadas = mutableListOf<Asignatura>()

                for (id in asignaturaIds) {
                    // Se asume que la interfaz API ha sido corregida para devolver Response<Asignatura>
                    val responseAsignatura = asignaturasRetrofit.asignaturaById(id)

                    if (responseAsignatura.isSuccessful) {
                        responseAsignatura.body()?.let {
                            asignaturasCargadas.add(it) // <-- Guardar el objeto Asignatura completo
                        }
                    } else {
                        // Manejo de error de una asignatura específica
                        _errorMessage.value = "Algunas asignaturas no pudieron cargarse. Falló ID: $id"
                    }
                }

                // 3. CORRECCIÓN: Asignar la lista de objetos Asignatura
                _asignaturas.value = asignaturasCargadas

            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.localizedMessage}"
                _asignaturas.value = null // Mantener null para indicar fallo
            }
        }
    }
}