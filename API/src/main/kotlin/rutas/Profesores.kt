package com.example.rutas.rutas

import com.example.DAO.DumbledorDAO
import com.example.DAO.dumbledorDAOImp
import com.example.model.Profesor
import io.ktor.server.routing.Route
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Route.adminDAO(){
    val dumbledorDAO : DumbledorDAO = dumbledorDAOImp

    // Listado de profesores
    get("/profesores") {
        val todosLosProfesores = dumbledorDAO.listar()
        call.respond(HttpStatusCode.OK, todosLosProfesores)
    }

    // Insertar profesor
    post("/profesores") {
        val profesor = call.receive<Profesor>()
        val ok = dumbledorDAO.insertar(profesor)

        if (ok) {
            call.respond(HttpStatusCode.Created, "Profesor creado correctamente")
        } else {
            call.respond(HttpStatusCode.Conflict, "No se pudo crear el profesor")
        }
    }

    // Borrar profesor por ID
    delete("/profesores/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
            "ID vacío o inválido en la URL",
            status = HttpStatusCode.BadRequest
        )

        val ok = dumbledorDAO.eliminar(id)
        if (ok) {
            call.respond(HttpStatusCode.OK, "Profesor con ID $id eliminado correctamente")
        } else {
            call.respond(HttpStatusCode.NotFound, "Profesor no encontrado")
        }
    }
}