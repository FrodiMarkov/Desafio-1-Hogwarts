package com.example.desafio1

import ViewModel.LoginViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.desafio1.databinding.ActivityMainBinding
import kotlin.jvm.java

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
                when {
                    it.roles.contains(1) -> startActivity(Intent(this, AlumnoActivity::class.java))
                    it.roles.contains(4) -> startActivity(Intent(this, DumbledorActivity::class.java).apply {
                        putExtra("usuario_id", it.id)
                    })
                    it.roles.contains(2) -> startActivity(Intent(this, ProfesorActivity::class.java).apply {
                        putExtra("usuario_id", it.id)
                    })
                    else -> Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }

        viewModel.error.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btLogin.setOnClickListener {
            val usuarioLogin = binding.etUser.text.toString()
            val passwordLogin = binding.etPassw.text.toString()
            viewModel.login(usuarioLogin, passwordLogin)
        }

        Glide.with(this)
            //que poner para que sea un gif y no una imagen
            .asGif()
            .load(R.drawable.background2)
            .into(binding.IVBackground)
    }
}