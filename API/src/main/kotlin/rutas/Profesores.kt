package com.example.rutas.rutas

import com.example.DAO.DumbledorDAO
import com.example.DAO.dumbledorDAOImp
import com.example.model.Usuario
import io.ktor.server.routing.Route
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.dumbledorDAO(){
    val dumbledorDAO : DumbledorDAO = dumbledorDAOImp

    get("/usuarios") {
        val usuarios = dumbledorDAO.listar()
        call.respond(HttpStatusCode.OK, usuarios)
    }

    post("/usuarios") {
        val usuario = call.receive<Usuario>()
        val ok = dumbledorDAO.insertar(usuario)

        if (ok) {
            call.respond(HttpStatusCode.Created, "Usuario creado correctamente")
        } else {
            call.respond(HttpStatusCode.Conflict, "No se pudo crear el usuario")
        }
    }

    delete("/usuarios/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()

        val ok = dumbledorDAO.eliminar(id)
        if (ok) {
            call.respond(HttpStatusCode.OK, "Usuario con ID $id eliminado correctamente")
        } else {
            call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
        }
    }
}