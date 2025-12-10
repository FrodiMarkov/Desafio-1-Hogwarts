package com.example.desafio1.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.FragmentoUsuarios
import com.example.desafio1.ViewModel.FragmentoUsuariosViewModel
import com.example.desafio1.databinding.ItemCardBinding
import com.example.model.UsuarioConRoles

class UsuariosAdapter(
    private val viewModel: FragmentoUsuariosViewModel,
    private val fragment: FragmentoUsuarios
) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    private var listaUsuarios: List<UsuarioConRoles> = emptyList()

    class UsuarioViewHolder(val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = ItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaUsuarios[position]

        holder.binding.tvNombre.text = usuario.nombre
        holder.binding.tvEmail.text = usuario.email
        holder.binding.tvExperiencia.text = "Experiencia: ${usuario.experiencia}"
        holder.binding.tvNivel.text = "Nivel: ${usuario.nivel}"
        holder.binding.tvCasa.text = "Casa: ${usuario.id_casa}"

        // CLICK NORMAL → Editar usuario
        holder.itemView.setOnClickListener {
            fragment.mostrarDialogEditarUsuario(usuario)
        }

        // CLICK LARGO → confirmar eliminación
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context

            AlertDialog.Builder(context)
                .setTitle("Eliminar usuario")
                .setMessage("¿Estás seguro de que deseas eliminar a ${usuario.nombre}?")
                .setPositiveButton("Sí") { dialog, _ ->
                    try {
                        viewModel.eliminarUsuario(usuario.id)
                        Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message ?: "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            true
        }
    }

    override fun getItemCount(): Int = listaUsuarios.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<UsuarioConRoles>) {
        listaUsuarios = newList
        notifyDataSetChanged()
    }
}
