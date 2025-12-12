package com.example.desafio1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio1.Adapters.AsignaturasAdapter
import com.example.desafio1.ViewModel.FragmentoAsignaturasViewModel
import com.example.desafio1.databinding.DialogEditarAsignaturaBinding
import com.example.desafio1.databinding.FragmentAsignaturaBinding
import model.Asignatura
import com.example.model.UsuarioConRoles

class FragmentoAsignatura : Fragment() {

    private val viewModel: FragmentoAsignaturasViewModel by viewModels()
    private lateinit var binding: FragmentAsignaturaBinding
    private lateinit var adapter: AsignaturasAdapter

    private var profesoresActuales: List<UsuarioConRoles> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAsignaturaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AsignaturasAdapter(viewModel, this)
        binding.rvAsignaturas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAsignaturas.adapter = adapter

        viewModel.cargarProfesores()
        viewModel.cargarAsignaturas()


        viewModel.profesores.observe(viewLifecycleOwner) { listaProfesores ->
            profesoresActuales = listaProfesores

            viewModel.asignaturas.value?.let { asignaturas ->
                adapter.updateData(asignaturas, profesoresActuales)
            }
        }

        viewModel.asignaturas.observe(viewLifecycleOwner) { listaAsignaturas ->
            adapter.updateData(listaAsignaturas, profesoresActuales)
        }

        viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
        }
    }

    fun mostrarDialogEditarAsignatura(asignatura: Asignatura) {
        val dialogBinding = DialogEditarAsignaturaBinding.inflate(LayoutInflater.from(requireContext()))

        // 1. Obtener datos del ViewModel (usamos la lista local actualizada por el observer)
        val profesores = profesoresActuales
        val nombresProfesores = profesores.map { it.nombre }

        val profesorActual = profesores.find { it.id == asignatura.id_profesor }

        val adapterDropdown = ArrayAdapter( // Renombrado para evitar conflicto con la variable 'adapter' global
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            nombresProfesores
        )

        dialogBinding.actvProfesor.setAdapter(adapterDropdown)
        dialogBinding.etNombreAsignatura.setText(asignatura.nombre)

        if (profesorActual != null) {
            // Establecer el nombre del profesor actual en el Dropdown (importante: false para no disparar listener)
            dialogBinding.actvProfesor.setText(profesorActual.nombre, false)
        }


        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Editar Asignatura")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = dialogBinding.etNombreAsignatura.text.toString()
                val nuevoProfesorNombre = dialogBinding.actvProfesor.text.toString()

                // **Mapear Nombre a ID**
                val nuevoProfesorId = profesores.find {
                    it.nombre.equals(nuevoProfesorNombre, ignoreCase = true)
                }?.id

                if (nuevoProfesorId == null) {
                    Toast.makeText(requireContext(), "Debe seleccionar un profesor válido.", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }

                val asignaturaEditada = asignatura.copy(
                    nombre = nuevoNombre,
                    id_profesor = nuevoProfesorId
                )
                viewModel.editarAsignatura(asignaturaEditada)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}