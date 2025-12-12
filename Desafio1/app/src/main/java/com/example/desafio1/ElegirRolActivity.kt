package com.example.desafio1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio1.Holders.ProfesorHolder
import com.example.desafio1.Holders.UsuarioHolder
import com.example.desafio1.databinding.ActivityElegirRolBinding
import com.example.desafio1.viewmodels.ElegirRolViewModel
import model.Asignatura

class ElegirRolActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElegirRolBinding

    private val viewModel: ElegirRolViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElegirRolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = UsuarioHolder.usuario

        if (usuario == null) {
            Toast.makeText(this, "Error: Datos de usuario no encontrados.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.asignaturasDelProfesor.observe(this) { asignaturas ->
            if (asignaturas.isNotEmpty()) {
                mostrarAlertDialog(asignaturas)
            } else if (viewModel.error.value == null) {
                Toast.makeText(this, "No tienes asignaturas asignadas.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btProfesor.setOnClickListener {
            viewModel.cargarAsignaturas(usuario.id)
        }

        binding.btAdmin.setOnClickListener {
            UsuarioHolder.usuario = usuario
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }
        binding.btnVolver.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    private fun mostrarAlertDialog(asignaturas: List<Asignatura>) {
        val nombresAsignaturas = asignaturas.map { it.nombre }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Elige la Asignatura a Gestionar")
            .setItems(nombresAsignaturas) { _, which ->
                val asignaturaSeleccionada = asignaturas[which]
                navegarAProfesorActivity(asignaturaSeleccionada)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navegarAProfesorActivity(asignatura: Asignatura) {
        var intent: Intent? = null
        when (asignatura.nombre) {
            "Hechizos" -> {
                intent = Intent(this, ProfesorHechizos::class.java)
            }

            else -> {
                Toast.makeText(this, "Actividad para ${asignatura.nombre} no implementada.", Toast.LENGTH_LONG).show()
                return
            }
        }
        ProfesorHolder.profesor = UsuarioHolder.usuario
        startActivity(intent)
        finish()
    }
}