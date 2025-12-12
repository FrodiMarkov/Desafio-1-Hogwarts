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
import com.example.desafio1.databinding.FragmentHechizosBinding

class FragmentoHechizosAlumno : Fragment() {

    private val viewModel: FragmentoHechizosAlumnoViewModel by viewModels()
    private lateinit var binding: FragmentHechizosBinding
    private lateinit var hechizoAdapter: HechizoAdapterAlumno


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHechizosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hechizoAdapter = HechizoAdapterAlumno { hechizo, usuarioActual ->
            viewModel.aprenderHechizo(hechizo, usuarioActual)
        }

        binding.rvListaHechizos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hechizoAdapter
        }

        viewModel.hechizos.observe(viewLifecycleOwner) { hechizosList ->
            hechizoAdapter.setHechizos(hechizosList)
        }

        viewModel.usuarioUpdateResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarHechizos()
    }
}