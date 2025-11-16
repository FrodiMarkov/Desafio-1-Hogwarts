package DAO

import Helpers.Database
import model.UsuarioConRoles

object dumbledorDAOImp : DumbledorDAO{
    override fun insertar(usuario: UsuarioConRoles): Boolean {
        val sql = """
        INSERT INTO usuario 
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

    override fun modificar(usuario: UsuarioConRoles): Boolean {
        val sql = """
        UPDATE usuario 
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
        val sql = "DELETE FROM usuario WHERE id = ?"
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

    override fun listarUsuariosConRoles(): List<UsuarioConRoles> {
        val sql = """
        SELECT u.id, u.nombre, u.email, u.contraseña, u.experiencia, u.id_casa, u.nivel,
               r.rol_id
        FROM usuario u
        LEFT JOIN roles_usuario r ON u.id = r.usuario_id
        ORDER BY u.id
    """.trimIndent()

        val connection = Database.getConnection() ?: return emptyList()

        connection.use { conn ->
            try {
                val stmt = conn.prepareStatement(sql)
                val rs = stmt.executeQuery()

                val listaUsuarios = mutableListOf<UsuarioConRoles>()
                var currentUsuarioId = -1
                var currentUsuario: UsuarioConRoles? = null
                var rolesTemp = mutableListOf<Int>()

                while (rs.next()) {
                    val usuarioId = rs.getInt("id")

                    if (usuarioId != currentUsuarioId) {
                        if (currentUsuario != null) {
                            currentUsuario.roles = rolesTemp
                            listaUsuarios.add(currentUsuario)
                        }

                        currentUsuarioId = usuarioId
                        rolesTemp = mutableListOf()
                        currentUsuario = UsuarioConRoles(
                            id = usuarioId,
                            nombre = rs.getString("nombre"),
                            email = rs.getString("email"),
                            contrasena = rs.getString("contraseña"),
                            experiencia = rs.getInt("experiencia"),
                            id_casa = rs.getInt("id_casa"),
                            nivel = rs.getInt("nivel"),
                            roles = mutableListOf()
                        )
                    }

                    val rolIdObj = rs.getObject("rol_id") as? Int
                    if (rolIdObj != null) {
                        rolesTemp.add(rolIdObj)
                    }
                }

                if (currentUsuario != null) {
                    currentUsuario.roles = rolesTemp
                    listaUsuarios.add(currentUsuario)
                }

                return listaUsuarios
            } catch (e: Exception) {
                e.printStackTrace()
                return emptyList()
            }
        }
    }

}