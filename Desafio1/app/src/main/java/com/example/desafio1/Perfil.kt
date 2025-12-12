package com.example.desafio1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio1.Holders.UsuarioHolder
import com.example.model.UsuarioConRoles
import com.example.desafio1.databinding.ActivityPerfilBinding

class Perfil : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.activityProfileMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userProfile = UsuarioHolder.usuario

        if (userProfile != null) {
            rellenarCampos(userProfile)
        } else {
            Toast.makeText(this, "Error: No se encontró información del usuario en el Holder.", Toast.LENGTH_LONG).show()
            finish()
        }
        binding.btBack2.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun rellenarCampos(user: UsuarioConRoles) {
        binding.apply {
            tvNombre.text = user.nombre
            tvEmail.text = "Email: ${user.email}"

            val houseName = getHouseNameById(user.id_casa)
            tvCasa.text = "Casa: $houseName"

            tvExperiencia.text = "Experiencia: ${user.experiencia} XP"
            tvNivel.text = "Nivel: ${user.nivel}"

        }
    }

    private fun getHouseNameById(id: Int): String {
        return when (id) {
            1 -> "Gryffindor"
            2 -> "Slytherin"
            3 -> "Hufflepuff"
            4 -> "Ravenclaw"
            else -> "Desconocida"
        }
    }
}