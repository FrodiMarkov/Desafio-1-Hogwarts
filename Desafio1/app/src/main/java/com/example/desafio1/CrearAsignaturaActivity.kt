package com.example.desafio1

import Api.retrofit
import android.content.Intent
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
import kotlinx.coroutines.launch

class CrearAsignaturaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearAsignaturaBinding
    private val ROL_PROFESOR = 2
    private val ROL_ALUMNO = 1

    private var listaProfesores: List<UsuarioConRoles> = emptyList()

    private var listaAlumnosBase: List<UsuarioConRoles> = emptyList()

    private var alumnosDisponibles: MutableList<UsuarioConRoles> = mutableListOf()
    private val alumnosSeleccionadosIds = mutableSetOf<Int>()
    private lateinit var adapterAlumnos: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearAsignaturaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarUsuarios()

        binding.btnGuardarAsignatura.setOnClickListener {
            guardarAsignatura()
        }
    }

    private fun cargarUsuarios() {
        lifecycleScope.launch {
            try {
                val todosUsuarios = retrofit.usuariosRetrofit.listarUsuariosConRoles()

                listaProfesores = todosUsuarios.filter { it.roles.contains(ROL_PROFESOR) }
                listaAlumnosBase = todosUsuarios.filter { it.roles.contains(ROL_ALUMNO) }

                alumnosDisponibles = listaAlumnosBase.toMutableList()

                configurarSelectorProfesor()
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

    private fun configurarSelectorAlumnos() {

        val nombresAlumnos = alumnosDisponibles.map { it.nombre }

        adapterAlumnos = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresAlumnos
        )
        binding.actvAlumnos.setAdapter(adapterAlumnos)

        binding.actvAlumnos.setOnItemClickListener { parent, view, position, id ->
            val nombreSeleccionado = parent.getItemAtPosition(position).toString()

            val alumnoSeleccionado = alumnosDisponibles.find { it.nombre == nombreSeleccionado }

            if (alumnoSeleccionado != null) {
                alumnosSeleccionadosIds.add(alumnoSeleccionado.id)

                añadirIdentificadorAlumno(alumnoSeleccionado)

                alumnosDisponibles.remove(alumnoSeleccionado)
                adapterAlumnos.clear()
                adapterAlumnos.addAll(alumnosDisponibles.map { it.nombre })
                adapterAlumnos.notifyDataSetChanged()
            }
            binding.actvAlumnos.setText("")
        }
    }

    private fun añadirIdentificadorAlumno(alumno: UsuarioConRoles) {
        val identificador = TextView(this).apply {
            text = "Identificador: ${alumno.nombre}"
            textSize = 14f
            setPadding(8)
            setBackgroundResource(android.R.color.darker_gray)
            tag = alumno.id

            setOnClickListener {
                alumnosSeleccionadosIds.remove(alumno.id)
                binding.chipGroupAlumnos.removeView(this)

                alumnosDisponibles.add(alumno)
                alumnosDisponibles.sortBy { it.nombre }

                adapterAlumnos.clear()
                adapterAlumnos.addAll(alumnosDisponibles.map { it.nombre })
                adapterAlumnos.notifyDataSetChanged()
            }
        }
        binding.chipGroupAlumnos.addView(identificador)
        binding.btnVolver.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun guardarAsignatura() {
        val nombre = binding.etNombreAsignatura.text.toString().trim()
        val profesorNombre = binding.actvProfesor.text.toString().trim()

        if (nombre.isBlank() || profesorNombre.isBlank()) {
            Toast.makeText(this, "Debe completar el nombre y seleccionar un profesor.", Toast.LENGTH_SHORT).show()
            return
        }

        val idProfesor = listaProfesores.find { it.nombre == profesorNombre }?.id

        if (idProfesor == null) {
            Toast.makeText(this, "Profesor no válido. Seleccione uno de la lista.", Toast.LENGTH_SHORT).show()
            return
        }

        val asignaturaNueva = CreacionAsignatura(
            nombre = nombre,
            idProfesor = idProfesor,
            idsAlumnos = alumnosSeleccionadosIds.toList()
        )

        lifecycleScope.launch {
            try {
                val response = retrofit.asignaturasRetrofit.crearAsignaturaConProfesoresYAlumnos(asignaturaNueva)

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