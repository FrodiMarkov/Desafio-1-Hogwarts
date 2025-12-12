package DAO

import model.Casa
import model.UsuarioConRoles

interface casasDAO{
    suspend fun datosCasas(): List<Casa>
    suspend fun modificarCasa(casa: Casa): Boolean
}