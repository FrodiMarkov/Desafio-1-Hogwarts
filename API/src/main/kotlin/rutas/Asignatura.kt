package rutas

import DAO.DumbledorDAO
import DAO.dumbledorDAOImp
import com.example.desafio1.model.CreacionAsignatura
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.log
import model.Asignatura

fun Route.asignaturas() {

    val dumbledorDAO: DumbledorDAO = dumbledorDAOImp

    get("/asignatura") {
        val listaAsignaturas = try {
            dumbledorDAO.todasAsignaturas()
        } catch (e: Exception) {
            println("ERROR AL OBTENER DATOS: ${e.message}")
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error en DAO o DB")
            return@get
        }
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

        val nuevoIdProfesor =
            asignatura.id_profesor

        if (idAsignatura != null && dumbledorDAO.modificarAsignatura(
                idAsignatura,
                asignatura.nombre,
                nuevoIdProfesor
            )
        ) {
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
        val asignaturaData = try {
            call.receive<CreacionAsignatura>()
        } catch (e: Exception) {
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
    get("/alumno_asignatura/{id_alumno}") {
        val idAlumno = call.parameters["id_alumno"]?.toIntOrNull()

        if (idAlumno == null) {
            call.respond(HttpStatusCode.BadRequest, "El ID del alumno es inválido o falta.")
            return@get
        }

        try {
            val relaciones = dumbledorDAO.listarAsignaturasAlumno(idAlumno)

            call.respond(relaciones)
        } catch (e: Exception) {
            call.application.log.error("Error al obtener relaciones para el alumno $idAlumno: ${e.message}", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                "Error al obtener la lista de relaciones para el alumno."
            )
        }
    }
}

