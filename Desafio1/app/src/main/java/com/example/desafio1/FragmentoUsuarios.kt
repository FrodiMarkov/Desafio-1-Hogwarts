package com.example.desafio1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio1.Adapters.UsuariosAdapter
import com.example.desafio1.ViewModel.FragmentoUsuariosViewModel
import com.example.desafio1.databinding.DialogEditarUsuarioBinding
import com.example.desafio1.databinding.FragmentUsuariosBinding
import com.example.model.UsuarioConRoles

class FragmentoUsuarios : Fragment() {

    private val viewModel: FragmentoUsuariosViewModel by viewModels()
    private lateinit var binding: FragmentUsuariosBinding
    private lateinit var adapter: UsuariosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsuariosBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsuariosAdapter(viewModel, this)
        binding.rvUsuarios.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsuarios.adapter = adapter


        viewModel.usuarios.observe(viewLifecycleOwner) { lista ->
            adapter.updateData(lista)
        }

        viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
        }

        viewModel.cargarUsuarios()

    }

    fun mostrarDialogEditarUsuario(usuario: UsuarioConRoles) {
        val dialogBinding = DialogEditarUsuarioBinding.inflate(LayoutInflater.from(requireContext()))

        dialogBinding.etNombre.setText(usuario.nombre)
        dialogBinding.etEmail.setText(usuario.email)
        dialogBinding.etContrasena.setText(usuario.contrasena)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Editar usuario")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val usuarioEditado = usuario.copy(
                    nombre = dialogBinding.etNombre.text.toString(),
                    email = dialogBinding.etEmail.text.toString(),
                    contrasena = dialogBinding.etContrasena.text.toString()
                )
                viewModel.editarUsuario(usuarioEditado)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}
