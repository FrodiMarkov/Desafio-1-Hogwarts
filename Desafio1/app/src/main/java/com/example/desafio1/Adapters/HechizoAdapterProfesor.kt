package com.example.desafio1.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.databinding.ItemCardHechizoBinding
import com.example.desafio1.model.Hechizo
class HechizoAdapterProfesor() : RecyclerView.Adapter<HechizoAdapterProfesor.HechizoViewHolder>() {

    private var hechizosList: List<Hechizo> = emptyList()

    class HechizoViewHolder(val binding: ItemCardHechizoBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HechizoViewHolder {
        val binding = ItemCardHechizoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HechizoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HechizoViewHolder, position: Int) {
        val hechizo = hechizosList[position]
        val context = holder.itemView.context

        holder.binding.apply {
            tvNombreHechizo.text = hechizo.nombre
            tvDescripcionHechizo.text = hechizo.descripcion
            tvNivelExperiencia.text = "Experiencia: ${hechizo.experiencia}"
        }

        holder.itemView.setOnClickListener {
            android.app.AlertDialog.Builder(context)
                .setTitle("Editar Hechizo")
                .setMessage("Has pulsado para editar: ${hechizo.nombre}. La edición real debe gestionarse con un Fragment/Dialog.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        holder.itemView.setOnLongClickListener {

            android.app.AlertDialog.Builder(context)
                .setTitle("Eliminar Hechizo")
                .setMessage("¿Estás seguro de que deseas eliminar el hechizo: ${hechizo.nombre}?")
                .setPositiveButton("Sí") { dialog, _ ->

                    Toast.makeText(context, "Hechizo '${hechizo.nombre}' eliminado (simulación)", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            true
        }
    }

    override fun getItemCount(): Int {
        return hechizosList.size
    }

    fun setHechizos(newHechizos: List<Hechizo>) {
        hechizosList = newHechizos
        notifyDataSetChanged()
    }
}