package com.example.desafio1.API

import com.example.desafio1.model.CreacionAsignatura
import model.AlumnoAsignatura
import model.Asignatura
import retrofit2.Response
import retrofit2.http.*

interface asignaturasAPI {

    @GET("asignatura")
    suspend fun listarAsignaturas(): List<Asignatura>

    @POST("asignatura")
    suspend fun crearAsignatura(@Body asignatura: Asignatura): Response<Unit>

    @PUT("asignatura/{id}")
    suspend fun modificarAsignatura(@Path("id") id: Int, @Body asignatura: Asignatura): Response<Unit>

    @DELETE("asignatura/{id}")
    suspend fun eliminarAsignatura(@Path("id") id: Int): Boolean

    @GET("asignatura/{id}")
    suspend fun asignaturaById(@Path("id") id: Int): Response<Asignatura>

    @POST("asignatura/completa") // Usamos una ruta específica para esta operación compleja
    suspend fun crearAsignaturaConProfesoresYAlumnos(@Body asignaturaData: CreacionAsignatura): Response<Unit>
    @GET("alumno_asignatura/{id_alumno}")
    suspend fun listarAlumnoAsignatura(@Path("id_alumno") idAlumno: Int?): Response<List<AlumnoAsignatura>>
}