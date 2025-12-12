package com.example.desafio1

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio1.ViewModel.FragmentoHechizosAlumnoViewModel
import com.example.desafio1.adapter.HechizoAdapterAlumno
import com.example.desafio1.databinding.FragmentHechizosBinding // Asume este binding

class FragmentoHechizosAlumno : Fragment() {

    private val viewModel: FragmentoHechizosAlumnoViewModel by viewModels()
    private lateinit var binding: FragmentHechizosBinding // Asume este binding
    private lateinit var hechizoAdapter: HechizoAdapterAlumno

    companion object {
        fun newInstance() = FragmentoHechizosAlumno()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHechizosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el adaptador: PASANDO LA REFERENCIA AL VIEWMODEL
        hechizoAdapter = HechizoAdapterAlumno { hechizo, usuarioActual ->
            // Este es el callback que se ejecuta al hacer clic en "Cerrar y Ganar EXP"
            viewModel.aprenderHechizo(hechizo, usuarioActual)
        }

        // Configuración del RecyclerView
        binding.rvListaHechizos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hechizoAdapter
        }

        // Observar la lista de hechizos
        viewModel.hechizos.observe(viewLifecycleOwner) { hechizosList ->
            hechizoAdapter.setHechizos(hechizosList)
        }

        // Observar el resultado de la actualización del usuario (éxito o error de la API)
        viewModel.usuarioUpdateResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
            }
        }

        // Observador de errores (general)
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // *** SOLUCIÓN: FORZAR RECARGA AL REANUDAR ***
    override fun onResume() {
        super.onResume()
        // Cuando la actividad/fragmento regresa a primer plano (por si hubo cambios)
        viewModel.cargarHechizos()
    }
}