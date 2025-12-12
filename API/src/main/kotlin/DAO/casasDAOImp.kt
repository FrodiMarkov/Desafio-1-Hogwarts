package DAO

import Helpers.Database
import model.Casa

object casasDAOImp: casasDAO {
    override suspend fun datosCasas(): List<Casa> {
        val connection = Database.getConnection()
        val casas = mutableListOf<Casa>()
        val sql = "SELECT * FROM casa"

        connection.createStatement().use { statement ->
            statement.executeQuery(sql).use { resultSet ->
                while (resultSet.next()) {
                    val casa = Casa(
                        id_casa = resultSet.getInt("id"),
                        nombreCasa = resultSet.getString("Nombre"),
                        puntosCasa = resultSet.getInt("puntos")
                    )
                    casas.add(casa)
                }
            }
        }
        return casas
    }


    override suspend fun modificarCasa(casa: Casa): Boolean {
        val sql = "UPDATE casa SET nombre = ?, puntos = ? WHERE id = ?"
        var exito = false
        val connection = Database.getConnection()
        connection.use { conn ->
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, casa.nombreCasa)
            stmt.setInt(2, casa.puntosCasa)
            stmt.setInt(3, casa.id_casa)
            try {
                exito = stmt.executeUpdate() > 0
            } catch (e: Exception) {
                e.printStackTrace()
                exito = false
            }
        }
        return exito
    }

}

