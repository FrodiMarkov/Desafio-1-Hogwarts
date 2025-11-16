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

    fun listarUsuariosConRoles(): List<UsuarioConRoles> {
        val sql = """
        SELECT u.id, u.nombre, u.email, u.contrasena, u.experiencia, u.id_casa, u.nivel,
               GROUP_CONCAT(r.rol_id) as roles
        FROM usuarios u
        JOIN roles_usuario r ON u.id = r.usuario_id
        GROUP BY u.id
    """.trimIndent()

        val connection = Database.getConnection() ?: return emptyList()

        connection.use { conn ->
            try {
                val stmt = conn.prepareStatement(sql)
                val rs = stmt.executeQuery()

                val lista = mutableListOf<UsuarioConRoles>() // usamos lista local para construir resultados

                while (rs.next()) {
                    val rolesString = rs.getString("roles") // ej: "1,2"
                    val rolesList = rolesString.split(",").map { it.toInt() }

                    val usuario = UsuarioConRoles(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        email = rs.getString("email"),
                        contrasena = rs.getString("contrasena"),
                        experiencia = rs.getInt("experiencia"),
                        id_casa = rs.getInt("id_casa"),
                        nivel = rs.getInt("nivel"),
                        roles = rolesList
                    )

                    lista.add(usuario)
                }

                return lista
            } catch (e: Exception) {
                e.printStackTrace()
                return emptyList()
            }
        }
    }
}