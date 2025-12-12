// Archivo: HechizoRoutes.kt (o similar)

import Helpers.Database
import com.example.dao.HechizosDAO
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.dao.hechizosDAOImp
import com.example.desafio1.model.Hechizo

fun Route.hechizo() {

    val hechizoDAO: HechizosDAO = hechizosDAOImp

    post("/hechizos") {
        val hechizo = try {
            call.receive<Hechizo>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Formato de datos de hechizo inválido")
            return@post
        }

        val connection = Database.getConnection()
        if (connection == null) {
            call.respond(HttpStatusCode.InternalServerError, "Error conectando a la base de datos")
            return@post
        }

        // Asigna el ID como 0 para indicar que es un nuevo registro
        val nuevoHechizo = hechizo.copy(id = 0)

        val hechizoId = hechizoDAO.insertar(nuevoHechizo) // Asume que insertar devuelve el nuevo ID

        if (hechizoId != null) {
            val hechizoCreado = nuevoHechizo.copy(id = hechizoId)
            call.respond(HttpStatusCode.Created, hechizoCreado)
        } else {
            call.respond(HttpStatusCode.Conflict, "No se pudo crear el hechizo")
        }
    }

    get("/hechizos") {
        val connection = Database.getConnection()
        if (connection == null) {
            call.respond(HttpStatusCode.InternalServerError, "Error conectando a la base de datos")
            return@get
        }

        val hechizos = hechizoDAO.listarTodos() // Asume que listarTodos devuelve List<Hechizo>

        call.respond(HttpStatusCode.OK, hechizos)
    }

    get("/hechizos/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "ID de hechizo no válido")
            return@get
        }

        val connection = Database.getConnection()
        if (connection == null) {
            call.respond(HttpStatusCode.InternalServerError, "Error conectando a la base de datos")
            return@get
        }

        val hechizo = hechizoDAO.obtenerPorId(id) // Asume que obtenerPorId devuelve Hechizo?

        if (hechizo != null) {
            call.respond(HttpStatusCode.OK, hechizo)
        } else {
            call.respond(HttpStatusCode.NotFound, "Hechizo con ID $id no encontrado")
        }
    }

    put("/hechizos/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "ID de hechizo no válido")
            return@put
        }

        val hechizoActualizado = try {
            call.receive<Hechizo>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Formato de datos de hechizo inválido")
            return@put
        }

        val connection = Database.getConnection()
        if (connection == null) {
            call.respond(HttpStatusCode.InternalServerError, "Error conectando a la base de datos")
            return@put
        }

        // Aseguramos que el objeto tiene el ID correcto
        val hechizoParaActualizar = hechizoActualizado.copy(id = id)

        val exito = hechizoDAO.actualizar(hechizoParaActualizar)

        if (exito) {
            call.respond(HttpStatusCode.OK, hechizoParaActualizar)
        } else {
            call.respond(HttpStatusCode.NotFound, "Hechizo con ID $id no encontrado o no se pudo actualizar")
        }
    }

    delete("/hechizos/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "ID de hechizo no válido")
            return@delete
        }

        val connection = Database.getConnection()
        if (connection == null) {
            call.respond(HttpStatusCode.InternalServerError, "Error conectando a la base de datos")
            return@delete
        }

        val exito = hechizoDAO.eliminar(id)

        if (exito) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound, "Hechizo con ID $id no encontrado")
        }
    }
}