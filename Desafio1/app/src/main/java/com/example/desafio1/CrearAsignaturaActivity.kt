package com.example.desafio1

import Api.HowartsNetwork
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.example.desafio1.databinding.ActivityCrearAsignaturaBinding
import com.example.desafio1.model.CreacionAsignatura
import com.example.model.UsuarioConRoles
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class CrearAsignaturaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearAsignaturaBinding
    private val ROL_PROFESOR = 2
    private val ROL_ALUMNO = 1

    private var listaProfesores: List<UsuarioConRoles> = emptyList()

    // Lista inmutable base de todos los alumnos (para referencia)
    private var listaAlumnosBase: List<UsuarioConRoles> = emptyList()

    // Lista mutable de alumnos disponibles para el dropdown
    private var alumnosDisponibles: MutableList<UsuarioConRoles> = mutableListOf()
    private val alumnosSeleccionadosIds = mutableSetOf<Int>()
    private lateinit var adapterAlumnos: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearAsignaturaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Cargar Usuarios (llama a configurarSelectorProfesor y configurarSelectorAlumnos)
        cargarUsuarios()

        // 3. Configurar el botón de guardar
        binding.btnGuardarAsignatura.setOnClickListener {
            guardarAsignatura()
        }
    }

    /**
     * Carga todos los usuarios y filtra profesores y alumnos.
     * Luego inicializa los selectores.
     */
    private fun cargarUsuarios() {
        lifecycleScope.launch {
            try {
                // La función suspend debe ser llamada aquí, dentro de la coroutine
                val todosUsuarios = HowartsNetwork.usuariosRetrofit.listarUsuariosConRoles()

                listaProfesores = todosUsuarios.filter { it.roles.contains(ROL_PROFESOR) }
                listaAlumnosBase = todosUsuarios.filter { it.roles.contains(ROL_ALUMNO) }

                // Inicializar la lista mutable para el dropdown
                alumnosDisponibles = listaAlumnosBase.toMutableList()

                configurarSelectorProfesor()
                // Llamamos a configurarSelectorAlumnos AHORA que tenemos los datos
                configurarSelectorAlumnos()
            } catch (e: Exception) {
                Toast.makeText(this@CrearAsignaturaActivity, "Error al cargar usuarios: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun configurarSelectorProfesor() {
        val nombresProfesores = listaProfesores.map { it.nombre }
        val adapterProfesor = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresProfesores
        )
        binding.actvProfesor.setAdapter(adapterProfesor)
    }

    /**
     * Configura el dropdown de selección múltiple para alumnos.
     */
    private fun configurarSelectorAlumnos() {

        // El adaptador usa solo los nombres de los alumnos disponibles (lista mutable)
        val nombresAlumnos = alumnosDisponibles.map { it.nombre }

        adapterAlumnos = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresAlumnos
        )
        binding.actvAlumnos.setAdapter(adapterAlumnos)

        // Listener para añadir alumnos al seleccionar uno del dropdown
        binding.actvAlumnos.setOnItemClickListener { parent, view, position, id ->
            val nombreSeleccionado = parent.getItemAtPosition(position).toString()

            // Buscar en la lista mutable actual de disponibles
            val alumnoSeleccionado = alumnosDisponibles.find { it.nombre == nombreSeleccionado }

            if (alumnoSeleccionado != null) {
                // 1. Añadir el alumno a la lista de seleccionados
                alumnosSeleccionadosIds.add(alumnoSeleccionado.id)

                // 2. Añadir el identificador a la UI
                añadirIdentificadorAlumno(alumnoSeleccionado)

                // 3. Eliminarlo de la lista de disponibles y actualizar el adapter
                alumnosDisponibles.remove(alumnoSeleccionado)
                adapterAlumnos.clear()
                adapterAlumnos.addAll(alumnosDisponibles.map { it.nombre })
                adapterAlumnos.notifyDataSetChanged()
            }
            // Limpiar el campo de texto
            binding.actvAlumnos.setText("")
        }
    }

    /**
     * Crea un TextView que actúa como "Identificador" y permite eliminar al alumno.
     */
    private fun añadirIdentificadorAlumno(alumno: UsuarioConRoles) {
        val identificador = TextView(this).apply {
            text = "Identificador: ${alumno.nombre}"
            textSize = 14f
            setPadding(8)
            setBackgroundResource(android.R.color.darker_gray)
            tag = alumno.id

            // Añadir un listener para ELIMINAR el alumno
            setOnClickListener {
                // 1. Eliminar el alumno del set de seleccionados y del layout
                alumnosSeleccionadosIds.remove(alumno.id)
                binding.chipGroupAlumnos.removeView(this)

                // 2. Devolver el alumno a la lista de disponibles (para que vuelva a aparecer en el dropdown)
                alumnosDisponibles.add(alumno)
                alumnosDisponibles.sortBy { it.nombre }

                // 3. Actualizar el adapter del dropdown
                adapterAlumnos.clear()
                adapterAlumnos.addAll(alumnosDisponibles.map { it.nombre })
                adapterAlumnos.notifyDataSetChanged()
            }
        }
        binding.chipGroupAlumnos.addView(identificador)
    }

    private fun guardarAsignatura() {
        val nombre = binding.etNombreAsignatura.text.toString().trim()
        val profesorNombre = binding.actvProfesor.text.toString().trim()

        if (nombre.isBlank() || profesorNombre.isBlank()) {
            Toast.makeText(this, "Debe completar el nombre y seleccionar un profesor.", Toast.LENGTH_SHORT).show()
            return
        }

        // Buscar el ID del profesor
        val idProfesor = listaProfesores.find { it.nombre == profesorNombre }?.id

        if (idProfesor == null) {
            Toast.makeText(this, "Profesor no válido. Seleccione uno de la lista.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el DTO para el backend
        val asignaturaNueva = CreacionAsignatura(
            nombre = nombre,
            idProfesor = idProfesor,
            idsAlumnos = alumnosSeleccionadosIds.toList()
        )

        lifecycleScope.launch {
            try {
                val response = HowartsNetwork.asignaturasRetrofit.crearAsignaturaConProfesoresYAlumnos(asignaturaNueva)

                if (response.isSuccessful) {
                    Toast.makeText(this@CrearAsignaturaActivity, "Asignatura creada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CrearAsignaturaActivity, "Error al crear: Código ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CrearAsignaturaActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}