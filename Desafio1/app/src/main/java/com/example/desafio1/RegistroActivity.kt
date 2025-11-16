package com.example.desafio1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio1.ViewModel.RegistroViewModel
import com.example.desafio1.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.registroResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_LONG).show()
            }
        }

        binding.btRegistrar.setOnClickListener {
            registrar()
        }
    }

    private fun registrar() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val contrasena = binding.etContrasena.text.toString().trim()
        val g = binding.sliderGryffindor.progress + 1
        val s = binding.sliderSlytherin.progress + 1
        val r = binding.sliderRavenclaw.progress + 1
        val h = binding.sliderHufflepuff.progress + 1

        viewModel.registrarUsuario(nombre, email, contrasena, g, s, r, h)
    }
}
