package com.example.desafio1.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.ViewModel.FragmentoAsignaturasViewModel
import com.example.desafio1.FragmentoAsignatura
import com.example.desafio1.databinding.ItemCardAsignaturaBinding
import com.example.model.UsuarioConRoles // Necesario para la lista de profesores
import model.Asignatura

class AsignaturasAdapter(
    private val viewModel: FragmentoAsignaturasViewModel,
    private val fragment: FragmentoAsignatura
) : RecyclerView.Adapter<AsignaturasAdapter.AsignaturaViewHolder>() {

    private var listaAsignaturas: List<Asignatura> = emptyList()

    // 🚨 NUEVO: Propiedad para almacenar la lista de profesores
    private var profesoresList: List<UsuarioConRoles> = emptyList()

    class AsignaturaViewHolder(val binding: ItemCardAsignaturaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsignaturaViewHolder {
        val binding = ItemCardAsignaturaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AsignaturaViewHolder(binding)
    }

    // Método auxiliar para buscar el nombre del profesor por ID
    private fun getNombreProfesor(id: Int?): String {
        return if (id == null || id == 0) { // Considerar 0 como no asignado si es el valor por defecto
            "No asignado"
        } else {
            // Buscamos el profesor en la lista que tenemos
            profesoresList.find { it.id == id }?.nombre ?: "Profesor desconocido"
        }
    }

    override fun onBindViewHolder(holder: AsignaturaViewHolder, position: Int) {
        val asignatura = listaAsignaturas[position]

        holder.binding.tvNombreAsignatura.text = asignatura.nombre

        val nombreProfesor = getNombreProfesor(asignatura.id_profesor) // Asegúrate de que tu modelo usa idProfesor (camelCase)
        holder.binding.tvNombreProfesor.text = "$nombreProfesor" // Asumo tvNombreProfesor existe y quieres ese formato


        // CLICK NORMAL → Editar asignatura
        holder.itemView.setOnClickListener {
            fragment.mostrarDialogEditarAsignatura(asignatura)
        }

        // CLICK LARGO → confirmar eliminación
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context

            AlertDialog.Builder(context)
                .setTitle("Eliminar Asignatura")
                .setMessage("¿Estás seguro de que deseas eliminar la asignatura: ${asignatura.nombre}?")
                .setPositiveButton("Sí") { dialog, _ ->
                    viewModel.eliminarAsignatura(asignatura.id)
                    Toast.makeText(context, "Asignatura eliminada", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            true
        }
    }

    override fun getItemCount(): Int = listaAsignaturas.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Asignatura>, newProfesores: List<UsuarioConRoles>) {
        listaAsignaturas = newList
        profesoresList = newProfesores
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Asignatura>) {
        listaAsignaturas = newList
        notifyDataSetChanged()
    }
}