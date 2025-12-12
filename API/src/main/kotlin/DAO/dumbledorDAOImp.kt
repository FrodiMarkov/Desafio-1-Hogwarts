package DAO

import Helpers.Database
import model.AlumnoAsignatura
import model.Asignatura
import model.Usuario
import model.UsuarioConRoles
import java.sql.Statement

object dumbledorDAOImp : DumbledorDAO{
    override fun seleccionarCasaEquilibrada(preferencias: Map<Int, Int>): Int {
        val connection = Database.getConnection()

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
        val sql = "UPDATE usuario SET nombre = ?, email = ?, contraseña = ?, experiencia = ?, id_casa = ?, nivel = ? WHERE id = ?"

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

        val connection = Database.getConnection()

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

    override fun todasAsignaturas(): List<Asignatura> {
        val lista = mutableListOf<Asignatura>()

        // CONSULTA CORREGIDA: Cambia 'pa.id_asignatura' por 'pa.asignatura_id'
        val sql = "SELECT a.id, a.nombre, pa.id_profesor FROM asignatura a LEFT JOIN profesor_asignatura pa ON a.id = pa.asignatura_id ORDER BY a.nombre"

        Database.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val idProfesor = rs.getInt("id_profesor")
                    val profesorId: Int = idProfesor

                    lista.add(
                        Asignatura(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            id_profesor = profesorId
                        )
                    )
                }
            }
        }
        return lista
    }

    override fun asignaturaById(id: Int): Asignatura? {

        // CONSULTA CORREGIDA: Cambia 'pa.id_asignatura' por 'pa.asignatura_id'
        val sql = "SELECT a.id, a.nombre, pa.id_profesor FROM asignatura a LEFT JOIN profesor_asignatura pa ON a.id = pa.asignatura_id WHERE a.id = ?"

        Database.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()

                return if (rs.next()) {
                    val idProfesor = rs.getInt("id_profesor")
                    val profesorId: Int = idProfesor

                    Asignatura(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        id_profesor = profesorId
                    )
                } else null
            }
        }
    }

    override fun crearAsignatura(nombre: String): Int {
        val sql = "INSERT INTO asignatura (nombre) VALUES (?)"

        Database.getConnection().use { conn ->
            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stmt ->
                stmt.setString(1, nombre)
                stmt.executeUpdate()

                val keys = stmt.generatedKeys
                return if (keys.next()) keys.getInt(1) else -1
            }
        }
    }

    override fun modificarAsignatura(id: Int, nombre: String, idProfesor: Int): Boolean {
        // 1. Sentencia para actualizar el nombre de la asignatura
        val sqlUpdateAsignatura = "UPDATE asignatura SET nombre = ? WHERE id = ?"

        val sqlInsertProfesor = "INSERT INTO profesor_asignatura (asignatura_id, id_profesor) VALUES (?, ?)"

        var filasAfectadas = 0

        Database.getConnection().use { conn ->
            try {
                // Iniciar Transacción
                conn.autoCommit = false

                // ==========================================================
                // Paso A: Actualizar el nombre de la asignatura
                // ==========================================================
                conn.prepareStatement(sqlUpdateAsignatura).use { stmt ->
                    stmt.setString(1, nombre)
                    stmt.setInt(2, id)
                    filasAfectadas += stmt.executeUpdate()
                }

                // ==========================================================
                // Paso B: Actualizar la relación profesor_asignatura
                // ==========================================================

                // B2. Insertar el nuevo profesor principal
                conn.prepareStatement(sqlInsertProfesor).use { stmt ->
                    stmt.setInt(1, id)
                    stmt.setInt(2, idProfesor)
                    stmt.executeUpdate()
                }

                // Si ambos pasos fueron exitosos
                conn.commit()

            } catch (e: Exception) {
                // Si algo falla, revertir los cambios
                conn.rollback()
                e.printStackTrace() // Imprimir el error para debug
                return false
            } finally {
                // Asegurarse de restaurar autoCommit a true al salir de la transacción
                conn.autoCommit = true
            }
        }

        return filasAfectadas > 0 // Retornamos true si se modificó al menos la tabla 'asignatura'
    }

    override fun borrarAsignatura(id: Int): Boolean {
        val sql = "DELETE FROM asignatura WHERE id = ?"

        Database.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
    }
    override fun crearAsignaturaCompleta(nombre: String, idProfesor: Int, idsAlumnos: List<Int>): Boolean {
        // Sentencias SQL
        val sqlInsertAsignatura = "INSERT INTO asignatura (nombre) VALUES (?)"
        val sqlInsertProfesor = "INSERT INTO profesor_asignatura (asignatura_id, id_profesor) VALUES (?, ?)"
        val sqlInsertAlumno = "INSERT INTO alumno_asignatura (id_asignatura, id_alumno) VALUES (?, ?)"

        var asignaturaId: Int? = null

        Database.getConnection().use { conn ->
            try {
                // Iniciar Transacción
                conn.autoCommit = false

                // ==========================================================
                // Paso A: Insertar Asignatura y obtener el ID generado
                // ==========================================================
                conn.prepareStatement(sqlInsertAsignatura, java.sql.Statement.RETURN_GENERATED_KEYS).use { stmt ->
                    stmt.setString(1, nombre)
                    stmt.executeUpdate()

                    // Obtener el ID de la asignatura recién creada
                    val rs = stmt.generatedKeys
                    if (rs.next()) {
                        asignaturaId = rs.getInt(1)
                    }
                }

                if (asignaturaId == null) throw Exception("No se pudo obtener el ID de la nueva asignatura.")

                // ==========================================================
                // Paso B: Asignar Profesor Principal
                // ==========================================================
                conn.prepareStatement(sqlInsertProfesor).use { stmt ->
                    stmt.setInt(1, asignaturaId!!)
                    stmt.setInt(2, idProfesor)
                    stmt.executeUpdate()
                }

                // ==========================================================
                // Paso C: Asignar Alumnos (Iteración y ejecución individual)
                // ==========================================================
                if (idsAlumnos.isNotEmpty()) {
                    conn.prepareStatement(sqlInsertAlumno).use { stmt ->
                        for (idAlumno in idsAlumnos) {
                            stmt.setInt(1, asignaturaId!!)
                            stmt.setInt(2, idAlumno)
                            // Ejecutar la inserción inmediatamente dentro del bucle
                            stmt.executeUpdate()
                        }
                    }
                }

                conn.commit()
                return true

            } catch (e: Exception) {
                conn.rollback()
                e.printStackTrace()
                return false
            } finally {
                conn.autoCommit = true
            }
        }
    }

    override fun listarAsignaturasAlumno(idAlumno: Int): List<AlumnoAsignatura> { // <-- ¡Cambiado! Acepta el ID

        val conexion = Database.getConnection()
        val alumnoAsignaturas = mutableListOf<AlumnoAsignatura>()

        // 1. Consulta SQL: Ahora siempre tiene un WHERE id_alumno = ?
        val sql = "SELECT id_asignatura, id_alumno FROM alumno_asignatura WHERE id_alumno = ?"

        // 2. Usar PreparedStatement para seguridad y eficiencia
        conexion.prepareStatement(sql).use { statement ->

            // 3. Asignar el parámetro al placeholder (?)
            statement.setInt(1, idAlumno)

            // 4. Ejecutar la consulta
            statement.executeQuery().use { rs ->

                while (rs.next()) {
                    val relacion = AlumnoAsignatura(
                        id_asignatura = rs.getInt("id_asignatura"),
                        id_alumno = rs.getInt("id_alumno")
                    )
                    alumnoAsignaturas.add(relacion)
                }
            }
        }

        return alumnoAsignaturas
    }
}