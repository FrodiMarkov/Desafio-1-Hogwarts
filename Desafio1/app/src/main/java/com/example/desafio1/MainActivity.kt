package com.example.desafio1

import Api.EncuestaNetwork.retrofit
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
//import necesario para glide
import com.bumptech.glide.Glide
import com.example.desafio1.API.usuariosAPI
import com.example.desafio1.databinding.ActivityMainBinding
import com.example.model.Usuario
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        binding.btLogin.setOnClickListener {
            val emailLogin = binding.etUser.text.toString()
            val passwordLogin = binding.etPassw.text.toString()

            lifecycleScope.launch {
                try {
                    val usuarios = retrofit.listarUsuarios()

                    val usuario = usuarios.find { it.email == emailLogin && it.contrasena == passwordLogin }

                    if (usuario != null) {
                        abrirSegunRol(Usuario.rol)
                    } else {
                        Toast.makeText(this@MainActivity, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error en la conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Glide.with(this)
            //que poner para que sea un gif y no una imagen
            .asGif()
            .load(R.drawable.background2) // reemplaza con tu archivo GIF
            .into(binding.IVBackground)
    }

    private fun abrirSegunRol(rol: Int) {
        val intent = when (rol) {
            1 -> Intent(this, AlumnoActivity::class.java)
            2 -> Intent(this, ProfesorActivity::class.java)
            3 -> Intent(this, AdminActivity::class.java)
            4 -> Intent(this, DumbledorActivity::class.java)
            else -> {
                Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                return
            }
        }
        startActivity(intent)
        finish()
    }
}