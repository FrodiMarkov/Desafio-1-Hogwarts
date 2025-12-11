package com.example.dao

import Helpers.Database
import com.example.desafio1.model.Hechizo
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

object hechizosDAOImp : HechizosDAO {

    private val TABLE_NAME = "hechizos"

    // --- CREATE ---
    override fun insertar(hechizo: Hechizo): Int? {
        val connection: Connection? = Database.getConnection()
        // CONSULTA INSERT EN UNA LÍNEA
        val sql = "INSERT INTO $TABLE_NAME (nombre, descripcion, experiencia, url_icono) VALUES (?, ?, ?, ?)"

        return connection?.use { conn ->
            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stmt ->
                stmt.setString(1, hechizo.nombre)
                stmt.setString(2, hechizo.descripcion)
                stmt.setInt(3, hechizo.experiencia)
                stmt.setString(4, hechizo.urlIcono)

                val affectedRows = stmt.executeUpdate()

                if (affectedRows > 0) {
                    stmt.generatedKeys.use { rs ->
                        if (rs.next()) return rs.getInt(1)
                    }
                }
                return null
            }
        }
    }

    // --- READ (Todos) ---
    override fun listarTodos(): List<Hechizo> {
        val connection: Connection? = Database.getConnection()
        val hechizos = mutableListOf<Hechizo>()
        val sql = "SELECT * FROM $TABLE_NAME" // Especificar columnas es buena práctica

        connection?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery(sql).use { rs ->
                    while (rs.next()) {
                        // Mapeo directo de ResultSet a Hechizo
                        val hechizo = Hechizo(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            descripcion = rs.getString("descripcion"),
                            experiencia = rs.getInt("experiencia"),
                            urlIcono = rs.getString("url_icono") // Asegúrate de que coincida con el nombre de la columna DB
                        )
                        hechizos.add(hechizo)
                    }
                }
            }
        }
        return hechizos
    }

    // --- READ (Por ID) ---
    override fun obtenerPorId(id: Int): Hechizo? {
        val connection: Connection? = Database.getConnection()
        // Especificar las columnas es buena práctica, aunque SELECT * también funcionaría
        val sql = "SELECT id, nombre, descripcion, experiencia, url_icono FROM $TABLE_NAME WHERE id = ?"

        return connection?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    // Si rs.next() es verdadero, significa que se encontró una fila
                    if (rs.next()) {
                        // Mapeo directo de ResultSet a Hechizo
                        return Hechizo(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            descripcion = rs.getString("descripcion"),
                            experiencia = rs.getInt("experiencia"),
                            urlIcono = rs.getString("url_icono")
                        )
                    }
                    return null // Devuelve null si no se encontró ninguna fila
                }
            }
        }
    }

    // --- UPDATE ---
    override fun actualizar(hechizo: Hechizo): Boolean {
        val connection: Connection? = Database.getConnection()
        // CONSULTA UPDATE EN UNA LÍNEA
        val sql = "UPDATE $TABLE_NAME SET nombre = ?, descripcion = ?, experiencia = ?, url_icono = ? WHERE id = ?"

        return connection?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, hechizo.nombre)
                stmt.setString(2, hechizo.descripcion)
                stmt.setInt(3, hechizo.experiencia)
                stmt.setString(4, hechizo.urlIcono)
                stmt.setInt(5, hechizo.id)

                stmt.executeUpdate() > 0
            }
        } ?: false
    }

    // --- DELETE ---
    override fun eliminar(id: Int): Boolean {
        val connection: Connection? = Database.getConnection()
        // CONSULTA DELETE EN UNA LÍNEA
        val sql = "DELETE FROM $TABLE_NAME WHERE id = ?"

        return connection?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)

                stmt.executeUpdate() > 0
            }
        } ?: false
    }
}