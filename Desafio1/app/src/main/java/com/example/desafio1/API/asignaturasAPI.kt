package com.example.desafio1.API

import com.example.desafio1.model.CreacionAsignatura
import model.AlumnoAsignatura
import model.Asignatura
import retrofit2.Response
import retrofit2.http.*

interface asignaturasAPI {

    /**
     * Obtiene todas las asignaturas.
     * Corresponde al endpoint Ktor: GET /asignaturas
     */
    @GET("asignatura")
    suspend fun listarAsignaturas(): List<Asignatura>

    /**
     * Crea una nueva asignatura.
     * Corresponde al endpoint Ktor: POST /asignaturas
     * Nota: En Ktor solo se usa el nombre para crear. Asumo que el cliente envía el objeto completo
     * y el servidor solo extrae el 'nombre'. Si solo necesitas enviar el nombre, ajusta el @Body.
     */
    @POST("asignatura")
    suspend fun crearAsignatura(@Body asignatura: Asignatura): Response<Unit>

    /**
     * Modifica una asignatura existente por ID.
     * Corresponde al endpoint Ktor: PUT /asignaturas/{id}
     */
    @PUT("asignatura/{id}")
    suspend fun modificarAsignatura(@Path("id") id: Int, @Body asignatura: Asignatura): Response<Unit>

    /**
     * Elimina una asignatura por ID.
     * Corresponde al endpoint Ktor: DELETE /asignaturas/{id}
     * Devolviendo un booleano (true si éxito, false si no se encontró o error).
     */
    @DELETE("asignatura/{id}")
    suspend fun eliminarAsignatura(@Path("id") id: Int): Boolean

    /**
     * Obtiene una asignatura por ID. (Opcional, basado en el GET Ktor)
     * Corresponde al endpoint Ktor: GET /asignaturas/{id}
     */
    @GET("asignatura/{id}")
    suspend fun asignaturaById(@Path("id") id: Int): Response<Asignatura>

    @POST("asignatura/completa") // Usamos una ruta específica para esta operación compleja
    suspend fun crearAsignaturaConProfesoresYAlumnos(@Body asignaturaData: CreacionAsignatura): Response<Unit>
    @GET("alumno_asignatura/{id_alumno}")
    suspend fun listarAlumnoAsignatura(@Path("id_alumno") idAlumno: Int?): Response<List<AlumnoAsignatura>>
}