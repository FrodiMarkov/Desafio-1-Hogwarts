package com.example.desafio1

import com.example.desafio1.ViewModel.LoginViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.desafio1.Holders.ProfesorHolder
import com.example.desafio1.Holders.UsuarioHolder
import com.example.desafio1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

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

        viewModel.usuarioLogueado.observe(this) { usuario ->
            usuario?.let {
                val roles = it.roles

                when {
                    roles.contains(4) -> {
                        // Pasa el objeto completo al Holder
                        UsuarioHolder.usuario = it

                        startActivity(Intent(this, DumbledorActivity::class.java))
                        finish()
                    }
                    // ----------------------------------------------------

                    roles.contains(1) -> {
                        startActivity(Intent(this, AlumnoActivity::class.java))
                        UsuarioHolder.usuario = it
                        finish()
                    }
                    // ----------------------------------------------------

                    roles.contains(2) && roles.contains(3) -> {
                        UsuarioHolder.usuario = it

                        // Inicia la Activity sin Intent.putExtra
                        startActivity(Intent(this, ElegirRolActivity::class.java))
                        finish()
                    }
                    roles.contains(2) -> {
                        ProfesorHolder.profesor = it

                        Log.d("Navegacion", "ProfesorHolder llenado con ID: ${ProfesorHolder.profesor?.id}")

                        startActivity(Intent(this, ProfesorActivity::class.java))
                        UsuarioHolder.usuario = it
                        finish()
                    }

                    roles.contains(3) -> {
                        startActivity(Intent(this, AdminActivity::class.java))
                        finish()
                    }

                    else -> Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                }
            }
        }


        viewModel.error.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        binding.btLogin.setOnClickListener {
            val usuarioLogin = binding.etUser.text.toString().trim()
            val passwordLogin = binding.etPassw.text.toString().trim()
            if (usuarioLogin.isNotEmpty() && passwordLogin.isNotEmpty()) {
                viewModel.login(usuarioLogin, passwordLogin)
            } else {
                Toast.makeText(this, "Ingresa email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar GIF de fondo
        Glide.with(this)
            .asGif()
            .load(R.drawable.background2) // Asegúrate que sea un GIF
            .into(binding.IVBackground)
    }
}
