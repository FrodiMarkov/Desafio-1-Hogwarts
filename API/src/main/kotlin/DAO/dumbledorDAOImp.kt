package DAO

import Helpers.Database
import model.Usuario
import model.UsuarioConRoles
import java.sql.Connection
import java.sql.Statement

object dumbledorDAOImp : DumbledorDAO{
    override fun seleccionarCasaEquilibrada(preferencias: Map<Int, Int>): Int {
        val connection = Database.getConnection() ?: return 1

        val casas = listOf(1, 2, 3, 4)
        val casaCounts = mutableMapOf<Int, Int>()

        val stmt = connection.prepareStatement(
            "SELECT id_casa, COUNT(*) AS cantidad FROM usuario GROUP BY id_casa"
        )
        val rs = stmt.executeQuery()

        while (rs.next()) {
            casaCounts[rs.getInt("id_casa")] = rs.getInt("cantidad")
        }

        // Asegurar que todas las casas existan aunque no tengan alumnos
        casas.forEach { casaCounts.putIfAbsent(it, 0) }

        // Seleccionar casa equilibrada basada en:
        // 1. casa con menos alumnos
        // 2. mejor preferencia del usuario
        val casaElegida = casas.sortedWith(
            compareBy<Int> { casaCounts[it] }
                .thenBy { preferencias[it] ?: 4 }
        ).first()

        return casaElegida
    }

    override fun insertar(usuario: Usuario): Int? {
        val connection = Database.getConnection() ?: return null

        val sql = "INSERT INTO usuario (nombre, email, contraseña, id_casa, experiencia, nivel) VALUES (?, ?, ?, ?, ?, ?)"

        // IMPORTANTE: solicitar keys generadas
        val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ps.setString(1, usuario.nombre)
        ps.setString(2, usuario.email)
        ps.setString(3, usuario.contrasena) // o usuario.password según tu modelo
        ps.setInt(4, usuario.idCasa)
        ps.setInt(5, 0)  // experiencia inicial
        ps.setInt(6, 1)  // nivel inicial

        val rows = ps.executeUpdate()
        if (rows > 0) {
            val rs = ps.generatedKeys
            if (rs.next()) {
                return rs.getInt(1)
            }
        }
        return null
    }


    override fun modificar(usuario: UsuarioConRoles): Boolean {
        val sql = "UPDATE usuario SET nombre = ?, email = ?, contrasena = ?, experiencia = ?, id_casa = ?, nivel = ? WHERE id = ?"

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

    override fun asignarRol(usuarioId: Int?, rolId: Int?): Boolean {
        val sql = "INSERT INTO roles_usuario (usuario_id, rol_id) VALUES (?, ?)"
        val connection = Database.getConnection() ?: return false

        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(1, usuarioId ?: return false)
            stmt.setInt(2, rolId ?: return false)

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