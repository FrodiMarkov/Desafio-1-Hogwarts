import Api.retrofit.asignaturasRetrofit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio1.Holders.UsuarioHolder
import kotlinx.coroutines.launch
import model.Asignatura

class AlumnoViewModel() : ViewModel() {

    private val _asignaturas = MutableLiveData<List<Asignatura>?>()
    val asignaturas: LiveData<List<Asignatura>?> = _asignaturas

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun cargarAsignaturas() {
        _errorMessage.value = null
        _asignaturas.value = null

        viewModelScope.launch {

            val idAlumno = UsuarioHolder.usuario?.id
            if (idAlumno == null) {
                _errorMessage.value = "Error: ID de alumno no disponible. Usuario no logueado."
                return@launch
            }

            try {
                val responseRelaciones = asignaturasRetrofit.listarAlumnoAsignatura(idAlumno)

                if (!responseRelaciones.isSuccessful) {
                    _errorMessage.value = "Error al cargar relaciones: Código ${responseRelaciones.code()}"
                    return@launch
                }

                val asignaturaIds = responseRelaciones.body()?.map { it.id_asignatura } ?: emptyList()

                if (asignaturaIds.isEmpty()) {
                    _asignaturas.value = emptyList()
                    return@launch
                }

                val asignaturasCargadas = mutableListOf<Asignatura>()

                for (id in asignaturaIds) {
                    val responseAsignatura = asignaturasRetrofit.asignaturaById(id)

                    if (responseAsignatura.isSuccessful) {
                        responseAsignatura.body()?.let {
                            asignaturasCargadas.add(it)
                        }
                    } else {
                        _errorMessage.value = "Algunas asignaturas no pudieron cargarse. Falló ID: $id"
                    }
                }

                _asignaturas.value = asignaturasCargadas

            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.localizedMessage}"
                _asignaturas.value = null
            }
        }
    }
}