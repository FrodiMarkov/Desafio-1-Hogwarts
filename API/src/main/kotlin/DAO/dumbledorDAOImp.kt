package com.example.DAO

import Helpers.Database
import com.example.model.Usuario

object dumbledorDAOImp : DumbledorDAO{
    override fun listar(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val sql = "SELECT * FROM usuarios"
        val connection = Database.getConnection() ?: return emptyList()

        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            val rs = stmt.executeQuery()
            while (rs.next()) lista.add(
                Usuario(
                    id = rs.getInt("id"),
                    nombre = rs.getString("nombre"),
                    email = rs.getString("email"),
                    contrasena = rs.getString("contrasena"),
                    experiencia = rs.getInt("experiencia"),
                    id_casa = rs.getInt("id_casa"),
                    nivel = rs.getInt("nivel")
                )
            )
        }

        return lista
    }
    override fun insertar(usuario: Usuario): Boolean {
        val sql = """
        INSERT INTO usuarios 
        (nombre, email, contrasena, experiencia, id_casa, nivel) 
        VALUES (?, ?, ?, ?, ?, ?)
    """.trimIndent()

        val connection = Database.getConnection() ?: return false

        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, usuario.nombre)
            stmt.setString(2, usuario.email)
            stmt.setString(3, usuario.contrasena)
            stmt.setInt(4, usuario.experiencia)
            stmt.setInt(5, usuario.id_casa)
            stmt.setInt(6, usuario.nivel)

            return try {
                stmt.executeUpdate() > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override fun modificar(usuario: Usuario): Boolean {
        val sql = """
        UPDATE usuarios 
        SET nombre = ?, email = ?, contrasena = ?, experiencia = ?, id_casa = ?, nivel = ? 
        WHERE id = ?
    """.trimIndent()

        val connection = Database.getConnection() ?: return false

        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, usuario.nombre)
            stmt.setString(2, usuario.email)
            stmt.setString(3, usuario.contrasena)
            stmt.setInt(4, usuario.experiencia)
            stmt.setInt(5, usuario.id_casa)
            stmt.setInt(6, usuario.nivel)
            stmt.setInt(7, usuario.id ?: return false) // id obligatorio

            return try {
                stmt.executeUpdate() > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override fun eliminar(id: Int?): Boolean {
        val sql = "DELETE FROM usuarios WHERE id = ?"
        val connection = Database.getConnection() ?: return false

        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(1, id ?: return false)

            return try {
                stmt.executeUpdate() > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

}