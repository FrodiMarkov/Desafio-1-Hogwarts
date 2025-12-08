package com.example.desafio1.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.R
import com.example.model.UsuarioConRoles

class UsuariosAdapter(
    private var usuarios: MutableList<UsuarioConRoles>,
    private val onItemClick: (UsuarioConRoles) -> Unit,
    private val onDeleteClick: (UsuarioConRoles) -> Unit,
    private val onEditClick: (UsuarioConRoles) -> Unit
) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvExperiencia: TextView = itemView.findViewById(R.id.tvExperiencia)
        val tvNivel: TextView = itemView.findViewById(R.id.tvNivel)
        val tvCasa: TextView = itemView.findViewById(R.id.tvCasa)
        val imgUsuario: ImageView = itemView.findViewById(R.id.imgUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]

        holder.tvNombre.text = usuario.nombre
        holder.tvEmail.text = usuario.email
        holder.tvExperiencia.text = "Experiencia: ${usuario.experiencia}"
        holder.tvNivel.text = "Nivel: ${usuario.nivel}"
        holder.tvCasa.text = "Casa: ${usuario.id_casa}"

        holder.itemView.setOnClickListener { onItemClick(usuario) }
        holder.itemView.setOnLongClickListener {
            onEditClick(usuario)
            true
        }
    }

    override fun getItemCount(): Int = usuarios.size

    fun updateData(lista: MutableList<UsuarioConRoles>) {
        usuarios = lista
        notifyDataSetChanged()
    }


}
