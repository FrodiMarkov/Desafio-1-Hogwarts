package com.example.desafio1

import Api.retrofit.usuariosRetrofit
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.desafio1.databinding.ActivityCrearUsuarioBinding
import com.example.model.UsuarioConRoles
import kotlinx.coroutines.launch

class CrearUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val email = binding.etEmail.text.toString()
            val exp = binding.etExperiencia.text.toString().toIntOrNull() ?: 0
            val nivel = binding.etNivel.text.toString().toIntOrNull() ?: 0
            val casaNombre = binding.etCasa.text.toString().trim()
            val contrasena = binding.etContrasena.text.toString()
            val casa: Int = when (casaNombre.lowercase()) {
                "gryffindor" -> 1
                "slytherin" -> 2
                "ravenclaw" -> 3
                "hufflepuff" -> 4
                else -> 0
            }

            val roleIds = mutableListOf<Int>()
            if (binding.cbAdmin.isChecked) roleIds.add(3)
            if (binding.cbProfesor.isChecked) roleIds.add(2)
            if (binding.cbAlumno.isChecked) roleIds.add(1)

            if (nombre.isBlank() || email.isBlank()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registro = UsuarioConRoles(
                id = 0,
                nombre = nombre,
                email = email,
                contrasena = contrasena,
                experiencia = exp,
                nivel = nivel,
                id_casa = casa,
                roles = roleIds
            )

            lifecycleScope.launch {
                try {
                    val response = usuariosRetrofit.registrarUsuarioConRoles(registro)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearUsuarioActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@CrearUsuarioActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CrearUsuarioActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
