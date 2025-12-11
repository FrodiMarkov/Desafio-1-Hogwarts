package com.example.desafio1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio1.ViewModel.HechizosViewModel
import com.example.desafio1.databinding.ActivityNuevosHechizosBinding

class NuevosHechizos : AppCompatActivity() {
    private lateinit var binding: ActivityNuevosHechizosBinding

    private val viewModel: HechizosViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNuevosHechizosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.errorMensaje.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.guardarExitoso.observe(this) { exito ->
            if (exito == true) {
                Toast.makeText(this, "Hechizo guardado exitosamente.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        binding.btnGuardarHechizo.setOnClickListener {
            guardarNuevoHechizo()
        }


        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarNuevoHechizo() {
        val nombre = binding.etNombreHechizo.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val experienciaStr = binding.etExperiencia.text.toString()

        viewModel.validarYGuardarHechizo(
            nombre = nombre,
            descripcion = descripcion,
            experienciaStr = experienciaStr
        )
    }
}