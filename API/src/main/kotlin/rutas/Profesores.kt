package rutas.rutas

import DAO.DumbledorDAO
import DAO.dumbledorDAOImp
import DAO.dumbledorDAOImp.seleccionarCasaEquilibrada
import Helpers.Database
import io.ktor.server.routing.Route
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import model.Registro
import model.Usuario

fun Route.dumbledorDAO(){
    val dumbledorDAO : DumbledorDAO = dumbledorDAOImp


    // Listar usuarios
    get("/usuario") {
        val usuarios = dumbledorDAO.listarUsuariosConRoles()
        call.respond(HttpStatusCode.OK, usuarios)
    }

    // 🔥 NUEVO: Registro con preferencias y casa equilibrada
    post("/usuario/registrar") {
        val req = call.receive<Registro>()

        val connection = Database.getConnection()
        if (connection == null) {
            call.respond(HttpStatusCode.InternalServerError, "Error conectando a la base de datos")
            return@post
        }

        // Mapear preferencias
        val preferencias = mapOf(
            1 to req.prefGry,
            2 to req.prefSly,
            3 to req.prefRav,
            4 to req.prefHuf
        )

        val idCasaElegida = seleccionarCasaEquilibrada(preferencias)

        // Crear usuario final
        val usuario = Usuario(
            nombre = req.nombre,
            email = req.email,
            contrasena = req.contraseña,
            idCasa = idCasaElegida
        )

        val usuarioId = dumbledorDAO.insertar(usuario)

        if (usuarioId != null) {
            dumbledorDAO.asignarRol(usuarioId, 1)
            call.respond(
                HttpStatusCode.Created,
                mapOf(
                    "id" to usuarioId,
                    "idCasa" to idCasaElegida
                )
            )
        } else {
            call.respond(HttpStatusCode.Conflict, "No se pudo crear el usuario")
        }
    }

    // Crear usuario manualmente (no equilibrado)
    post("/usuario") {
        val usuario = call.receive<Usuario>()
        val usuarioId = dumbledorDAO.insertar(usuario)

        if (usuarioId != null) {
            dumbledorDAO.asignarRol(usuarioId, 1)
            call.respond(HttpStatusCode.Created, "Usuario creado correctamente")
        } else {
            call.respond(HttpStatusCode.Conflict, "No se pudo crear el usuario")
        }
    }

    // Eliminar usuario
    delete("/usuario/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()

        val ok = dumbledorDAO.eliminar(id)
        if (ok) {
            call.respond(HttpStatusCode.OK, "Usuario con ID $id eliminado correctamente")
        } else {
            call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
        }
    }
}