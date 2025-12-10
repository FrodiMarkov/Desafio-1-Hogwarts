package Helpers

import Helpers.Parametros
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private val url = "jdbc:mysql://localhost:" +  Parametros.puerto + "/" + Parametros.bbdd // Cambia según tu configuración
    private val user = Parametros.usuario
    private val password = Parametros.passwd

    fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }
}