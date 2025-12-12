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

        // 1. CORRECCIÓN CRÍTICA: Inicializar el ViewModel ANTES de usarlo
        viewModel = ViewModelProvider(this).get(AlumnoViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Observación del Estado del ViewModel ---

        // 2. Observador de Errores (se mantiene el original)
        viewModel.errorMessage.observe(this) { message ->
            // Si hay un error y no hay lista de asignaturas (null), lo muestra.
            if (message != null && viewModel.asignaturas.value == null) {
                Toast.makeText(this, "Error de Carga: $message", Toast.LENGTH_LONG).show()
                // Es crucial tener esta función en el ViewModel:
                // viewModel.clearErrorMessage()
            }
        }

        // 3. Observador de Asignaturas (Lista final) - Se usa la implementación combinada
        viewModel.asignaturas.observe(this) { asignaturas ->
            // Solo actuamos si el valor no es null (carga finalizada)
            if (asignaturas != null) {
                when {
                    asignaturas.isEmpty() -> {
                        // Lista vacía: usar Toast simple
                        Toast.makeText(
                            this,
                            "El alumno no está inscrito en ninguna asignatura.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        // Éxito: usar el diálogo seleccionable
                        mostrarDialogoSeleccion(asignaturas)
                    }
                }
            }
        }

        // --- Conexión de Botones ---

        binding.btVerPerfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        binding.btVerAsignaturas.setOnClickListener {
            // Dispara la carga de datos en el ViewModel
            viewModel.cargarAsignaturas()
        }
    } // Fin de onCreate

    // 4. Implementación de la función auxiliar de Diálogo (Mantenida)
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

    // 5. Implementación de la función de Navegación (Mantenida)
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