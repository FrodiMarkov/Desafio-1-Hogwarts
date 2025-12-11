package com.example.dao

import com.example.desafio1.model.Hechizo


interface HechizosDAO {
    fun insertar(hechizo: Hechizo): Int?

    // READ
    fun listarTodos(): List<Hechizo>
    fun obtenerPorId(id: Int): Hechizo?

    // UPDATE
    fun actualizar(hechizo: Hechizo): Boolean

    // DELETE
    fun eliminar(id: Int): Boolean
}