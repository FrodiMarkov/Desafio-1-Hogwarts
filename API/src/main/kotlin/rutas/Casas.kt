package rutas

import DAO.casasDAO
import DAO.casasDAOImp
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import model.Casa


fun Route.casasRoutes() {
    val dao: casasDAO = casasDAOImp

    get("/casas") {
        val casas = dao.datosCasas()
        call.respond(HttpStatusCode.OK, casas)
    }

    put("/casas") {
        try {
            val casaModificada = call.receive<Casa>()
            val exito = dao.modificarCasa(casaModificada)

            if (exito) {
                call.respond(HttpStatusCode.OK, "Casa modificada exitosamente.")
            } else {
                call.respond(HttpStatusCode.BadRequest, "No se pudo modificar la casa. Verifique el ID.")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor.")
        }
    }
}