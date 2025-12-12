package com.example.desafio1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio1.Adapter.HechizoAdapterProfesor // Asegúrate de que esta clase esté importada
import com.example.desafio1.ViewModel.FragmentoViewModelProfesorHechizos
import com.example.desafio1.databinding.FragmentHechizosBinding

class FragmentoHechizos : Fragment() {

    private val viewModel: FragmentoViewModelProfesorHechizos by viewModels()
    private lateinit var binding: FragmentHechizosBinding
    private lateinit var hechizoAdapter: HechizoAdapterProfesor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHechizosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el adaptador
        hechizoAdapter = HechizoAdapterProfesor()

        // Configuración del RecyclerView
        binding.rvListaHechizos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hechizoAdapter
        }

        // Observar los LiveData (solo se configuran una vez)
        viewModel.hechizos.observe(viewLifecycleOwner) { hechizosList ->
            hechizoAdapter.setHechizos(hechizosList)
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