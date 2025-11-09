package com.example.DAO

import Helpers.Database
import com.example.model.Profesor

object dumbledorDAOImp : DumbledorDAO{
    override fun listar(): List<Profesor> {
        val lista = mutableListOf<Profesor>()
        val sql = "SELECT * FROM profesores"
        val connection = Database.getConnection() ?: return emptyList()
        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            val rs = stmt.executeQuery()
            while (rs.next()) lista.add(
                Profesor(
                    id = rs.getInt("id"),
                    Nombre = rs.getString("Nombre"),
                    Rol = rs.getInt("Rol"),
                    Clase = rs.getString("Clase")
                )
            )
        }
        return lista
    }
    override fun insertar(profesor: Profesor): Boolean {
        val sql = "INSERT INTO profesores (Nombre, Rol, Clase) VALUES (?, ?, ?)"
        val connection = Database.getConnection() ?: return false
        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, profesor.Nombre)
            stmt.setInt(2, profesor.Rol)
            stmt.setString(3, profesor.Clase)

            return try {
                stmt.executeUpdate() > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override fun modificar(profesor: Profesor): Boolean {
        val sql = "UPDATE profesores SET Nombre = ?, Rol = ?, Clase = ? WHERE id = ?"
        val connection = Database.getConnection() ?: return false

        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)

            stmt.setString(1, profesor.Nombre) // Nombre
            stmt.setInt(2, profesor.Rol)       // Rol
            stmt.setString(3, profesor.Clase)  // Clase
            stmt.setInt(4, profesor.id)        // id (para el WHERE)

            return try {
                stmt.executeUpdate() > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }


    override fun eliminar(id: Int): Boolean {
        val sql = "DELETE FROM profesores WHERE Id = ?"
        val connection = Database.getConnection() ?: return false
        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(1, id)
            return try { stmt.executeUpdate() > 0 } catch (e: Exception) { e.printStackTrace(); false }
        }
    }
}