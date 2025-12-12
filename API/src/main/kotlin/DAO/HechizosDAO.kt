package com.example.dao

import com.example.desafio1.model.Hechizo


interface HechizosDAO {
    fun insertar(hechizo: Hechizo): Int?

    fun listarTodos(): List<Hechizo>
    fun obtenerPorId(id: Int): Hechizo?

    fun actualizar(hechizo: Hechizo): Boolean

    fun eliminar(id: Int): Boolean
}