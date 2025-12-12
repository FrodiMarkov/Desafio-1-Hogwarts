package com.example.desafio1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio1.databinding.ActivityAlumnoHechizosBinding // Importar el binding

// Nota: El archivo activity_alumno_hechizos.xml debe generar la clase ActivityAlumnoHechizosBinding

class AlumnoHechizos : AppCompatActivity() {

    private lateinit var binding: ActivityAlumnoHechizosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar el Binding
        binding = ActivityAlumnoHechizosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplicar insets de ventana (usando binding.main si está disponible, o binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Lógica del Botón Volver ---
        // Implementación similar a ProfesorHechizos.kt, pero volviendo a la Activity principal (MainActivity)
        binding.btnVolver.setOnClickListener {
            val intent = Intent(this, AlumnoActivity::class.java)
            // Opcional: Intent(this, MainActivity::class.java) si quieres volver a la pantalla de login/inicio.
            startActivity(intent)
            finish()
        }

        // NO se implementa binding.btnAnadirHechizo.setOnClickListener porque el botón no existe en la vista del alumno.
    }
}