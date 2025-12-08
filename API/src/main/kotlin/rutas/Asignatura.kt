package rutas

import DAO.DumbledorDAO
import DAO.dumbledorDAOImp
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import model.Asignatura

fun Route.dumbledorDAO() {

    val dumbledorDAO: DumbledorDAO = dumbledorDAOImp

    get("/asignaturas") {
        call.respond(dumbledorDAO.todasAsignaturas())
    }

    get("/asignaturas/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "ID inválido")
            return@get
        }

        val asignatura = dumbledorDAO.asignaturaById(id)

        if (asignatura == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(asignatura)
        }
    }

    post("/asignaturas") {
        val asignatura = call.receive<Asignatura>()
        val exito = dumbledorDAO.crearAsignatura(asignatura.nombre)
        if (exito != null) {
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "No se pudo crear la asignatura")
        }
    }

    put("/asignaturas/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        val asignatura = call.receive<Asignatura>()

        if (id != null && dumbledorDAO.modificarAsignatura(id, asignatura.nombre)) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    delete("/asignaturas/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()

        if (id != null && dumbledorDAO.borrarAsignatura(id)) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
