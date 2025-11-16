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
import kotlinx.coroutines.launch
import kotlin.jvm.java

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

        binding.btRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btLogin.setOnClickListener {
            val emailLogin = binding.etUser.text.toString()
            val passwordLogin = binding.etPassw.text.toString()

            lifecycleScope.launch {
                try {
                    val usuarios = retrofit.listarUsuariosConRoles()
                    val usuario = usuarios.find { it.nombre == emailLogin && it.contrasena == passwordLogin }

                    if (usuario != null) {
                        when {
                            usuario.roles.contains(1) -> {
                                val intent = Intent(this@MainActivity, AlumnoActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            usuario.roles.contains(4) -> {
                                val intent = Intent(this@MainActivity, DumbledorActivity::class.java)
                                intent.putExtra("usuario_id", usuario.id)
                                startActivity(intent)
                                finish()
                            }
                            usuario.roles.contains(2) -> {
                                val intent = Intent(this@MainActivity, ProfesorActivity::class.java)
                                intent.putExtra("usuario_id", usuario.id)
                                startActivity(intent)
                                finish()
                            }
                            else -> Toast.makeText(this@MainActivity, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                        }
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
}