package com.example.desafio1

import AlumnoViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio1.Holders.UsuarioHolder
import com.example.desafio1.databinding.ActivityAlumnoBinding
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import model.Asignatura // Asegúrate de que este import es correcto

class AlumnoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlumnoBinding
    private lateinit var viewModel: AlumnoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAlumnoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AlumnoViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.errorMessage.observe(this) { message ->
            if (message != null && viewModel.asignaturas.value == null) {
                Toast.makeText(this, "Error de Carga: $message", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.asignaturas.observe(this) { asignaturas ->
            if (asignaturas != null) {
                when {
                    asignaturas.isEmpty() -> {
                        Toast.makeText(
                            this,
                            "El alumno no está inscrito en ninguna asignatura.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        mostrarDialogoSeleccion(asignaturas)
                    }
                }
            }
        }


        binding.btVerPerfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        binding.btVerAsignaturas.setOnClickListener {
            viewModel.cargarAsignaturas()
        }
    }
    private fun mostrarDialogoSeleccion(asignaturas: List<Asignatura>) {
        val nombresAsignaturas = asignaturas.map { it.nombre }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Elige la Asignatura a Gestionar")
            .setItems(nombresAsignaturas) { _, which ->
                val asignaturaSeleccionada = asignaturas[which]
                navegarAAsignaturaDetalle(asignaturaSeleccionada)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navegarAAsignaturaDetalle(asignatura: Asignatura) {
        var intent: Intent?
        when (asignatura.nombre) {
            "Hechizos" -> {
                intent = Intent(this, AlumnoHechizos::class.java)
            }
            else -> {
                Toast.makeText(
                    this,
                    "Actividad para ${asignatura.nombre} no implementada.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }
        startActivity(intent)
        finish()
    }
}