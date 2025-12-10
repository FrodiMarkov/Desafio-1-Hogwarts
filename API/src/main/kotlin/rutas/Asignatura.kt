package rutas

import DAO.DumbledorDAO
import DAO.dumbledorDAOImp
import com.example.desafio1.model.CreacionAsignatura
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import model.Asignatura

fun Route.asignaturas() {

    val dumbledorDAO: DumbledorDAO = dumbledorDAOImp

    // 1. Intenta obtener la lista (lo que probablemente falla)
    get("/asignatura") {
        // 1. Intenta obtener la lista (lo que probablemente falla)
        val listaAsignaturas = try {
            dumbledorDAO.todasAsignaturas()
        } catch (e: Exception) {
            // Imprime el error real en la consola de Ktor
            println("ERROR AL OBTENER DATOS: ${e.message}")
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error en DAO o DB")
            return@get
        }

        // 2. Si llegas aquí, el DAO funcionó. Ahora intenta serializar la respuesta.
        call.respond(listaAsignaturas)
    }

    get("/asignatura/{id}") {
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

    post("/asignatura") {
        val asignatura = call.receive<Asignatura>()
        val exito = dumbledorDAO.crearAsignatura(asignatura.nombre)
        if (exito != null) {
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "No se pudo crear la asignatura")
        }
    }

    put("/asignatura/{id}") {
        val idAsignatura = call.parameters["id"]?.toIntOrNull()
        val asignatura = call.receive<Asignatura>()

        // Suponiendo que el nuevo ID del profesor se recibe en asignatura.idProfesor
        val nuevoIdProfesor = asignatura.id_profesor // Debes asegurarte de que este campo exista en el modelo transferido

        if (idAsignatura != null && dumbledorDAO.modificarAsignatura(idAsignatura, asignatura.nombre, nuevoIdProfesor)) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    delete("/asignatura/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()

        if (id != null && dumbledorDAO.borrarAsignatura(id)) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/asignatura/completa") {
        // 1. Recibir el DTO completo del cliente Android
        val asignaturaData = try {
            call.receive<CreacionAsignatura>()
        } catch (e: Exception) {
            // En caso de error de deserialización (JSON mal formado)
            call.respond(HttpStatusCode.BadRequest, "Datos inválidos en el cuerpo de la solicitud.")
            return@post
        }

        // 2. Ejecutar la lógica de creación en el DAO
        val exito = dumbledorDAO.crearAsignaturaCompleta(
            asignaturaData.nombre,
            asignaturaData.idProfesor,
            asignaturaData.idsAlumnos
        )

        if (exito) {
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "No se pudo crear la asignatura y sus relaciones.")
        }
    }
}

