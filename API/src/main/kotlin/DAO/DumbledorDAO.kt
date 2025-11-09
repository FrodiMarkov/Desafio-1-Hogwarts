package com.example.DAO

import com.example.model.Profesor

interface DumbledorDAO {
    fun insertar(profesor: Profesor): Boolean
    fun modificar(profesor: Profesor): Boolean
    fun eliminar(id: Int): Boolean
    fun listar(): List<Profesor>
}