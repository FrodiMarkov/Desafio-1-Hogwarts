// Archivo: com/example/desafio1/adapter/HechizoAdapterAlumno.kt

package com.example.desafio1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.R
import com.example.desafio1.model.Hechizo
import com.example.desafio1.Holders.UsuarioHolder
import com.example.model.UsuarioConRoles // Asume la existencia de la clase UsuarioConRoles

// ACEPTA UNA FUNCIÓN CALLBACK para notificar al ViewModel
class HechizoAdapterAlumno(
    private val onAprenderHechizo: (Hechizo, UsuarioConRoles) -> Unit
) : RecyclerView.Adapter<HechizoAdapterAlumno.HechizoViewHolder>() {

    private var hechizosList: List<Hechizo> = emptyList()

    inner class HechizoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context: Context = itemView.context

        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreHechizo)
        private val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionHechizo)
        private val tvExperiencia: TextView = itemView.findViewById(R.id.tvNivelExperiencia)

        fun bind(hechizo: Hechizo) {
            tvNombre.text = hechizo.nombre
            tvDescripcion.text = hechizo.descripcion
            tvExperiencia.text = "Experiencia: ${hechizo.experiencia}"

            itemView.setOnClickListener {
                mostrarDialogoHechizo(hechizo)
            }
        }

        private fun mostrarDialogoHechizo(hechizo: Hechizo) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(hechizo.nombre)

            val mensaje = "Descripción:\n${hechizo.descripcion}\n\n" +
                    "Ganancia de Experiencia:\n${hechizo.experiencia} EXP"

            builder.setMessage(mensaje)

            builder.setPositiveButton("Cerrar") { dialog, _ ->

                val usuarioActual = UsuarioHolder.usuario

                if (usuarioActual != null) {
                    // DELEGA la acción de actualización al callback del ViewModel
                    onAprenderHechizo(hechizo, usuarioActual)
                } else {
                    Toast.makeText(context, "Error: Usuario no logueado.", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }

            builder.create().show()
        }
    }

    // ... (Métodos onCreateViewHolder, onBindViewHolder, getItemCount y setHechizos sin cambios) ...

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HechizoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_hechizo, parent, false)
        return HechizoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HechizoViewHolder, position: Int) {
        holder.bind(hechizosList[position])
    }

    override fun getItemCount(): Int = hechizosList.size

    fun setHechizos(hechizos: List<Hechizo>) {
        this.hechizosList = hechizos
        notifyDataSetChanged()
    }
}