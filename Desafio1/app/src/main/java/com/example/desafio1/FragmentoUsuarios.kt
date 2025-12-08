package com.example.desafio1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.Adapters.UsuariosAdapter
import com.example.desafio1.model.Registro
import com.example.desafio1.model.Usuario
import com.example.model.UsuarioConRoles
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentoUsuarios : Fragment() {

    companion object {
        fun newInstance() = FragmentoUsuarios()
    }

    private val viewModel: FragmentoUsuariosViewModel by viewModels()
    private lateinit var adapter: UsuariosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvUsuarios)
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarUsuario)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = UsuariosAdapter(
            usuarios = mutableListOf(),
            onItemClick = { usuario ->
                Toast.makeText(requireContext(), "Usuario: ${usuario.nombre}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { usuario ->
                viewModel.eliminarUsuario(usuario.id) // usar ID
            },
            onEditClick = { usuario ->
                mostrarDialogEditarUsuario(usuario)
            }
        )

        recyclerView.adapter = adapter

        // Observar cambios en la lista de usuarios
        viewModel.usuarios.observe(viewLifecycleOwner, Observer { lista ->
            adapter.updateData(lista.toMutableList())
        })

        // Observar mensajes de error
        viewModel.mensajeError.observe(viewLifecycleOwner, Observer { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
        })

        // Cargar usuarios desde la API
        viewModel.cargarUsuarios()
    }

    fun mostrarDialogEditarUsuario(usuario: UsuarioConRoles) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_editar_usuario, null)

        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etContrasena = dialogView.findViewById<EditText>(R.id.etContrasena)

        etNombre.setText(usuario.nombre)
        etEmail.setText(usuario.email)
        etContrasena.setText(usuario.contrasena)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Editar usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val usuarioEditado = usuario.copy(
                    nombre = etNombre.text.toString(),
                    email = etEmail.text.toString(),
                    contrasena = etContrasena.text.toString()
                )
                viewModel.editarUsuario(usuarioEditado)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

}
