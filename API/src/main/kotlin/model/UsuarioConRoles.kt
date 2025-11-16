package model
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioConRoles(
    val id: Int,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val experiencia: Int,
    val id_casa: Int,
    val nivel: Int,
    var roles: List<Int>
)