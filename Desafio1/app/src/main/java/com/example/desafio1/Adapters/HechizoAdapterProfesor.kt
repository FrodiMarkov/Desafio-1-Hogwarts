package com.example.desafio1.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.databinding.ItemCardHechizoBinding
import com.example.desafio1.model.Hechizo // Asegúrate de que este modelo exista

// El adaptador ahora recibe una instancia de FragmentoHechizos
class HechizoAdapterProfesor() : RecyclerView.Adapter<HechizoAdapterProfesor.HechizoViewHolder>() {

    private var hechizosList: List<Hechizo> = emptyList()

    class HechizoViewHolder(val binding: ItemCardHechizoBinding) : RecyclerView.ViewHolder(binding.root) {
        // La lógica de bind se maneja en onBindViewHolder
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
        val context = holder.itemView.context // Necesario para crear los AlertDialogs

        // **Lógica de enlace de datos**
        holder.binding.apply {
            tvNombreHechizo.text = hechizo.nombre
            tvDescripcionHechizo.text = hechizo.descripcion
            // Asumiendo que tu modelo tiene una propiedad 'experiencia'
            tvNivelExperiencia.text = "Experiencia: ${hechizo.experiencia}"
        }

        holder.itemView.setOnClickListener {
            android.app.AlertDialog.Builder(context)
                .setTitle("Editar Hechizo")
                .setMessage("Has pulsado para editar: ${hechizo.nombre}. La edición real debe gestionarse con un Fragment/Dialog.")
                .setPositiveButton("OK") { dialog, _ ->
                    // Aquí iría la lógica para abrir el diálogo de edición complejo
                    // Pero por simplicidad en el Adapter, solo cerramos.
                    dialog.dismiss()
                }
                .show()
        }

        // CLICK LARGO → Mostrar AlertDialog para confirmar eliminación (Lógica del primer ejemplo)
        holder.itemView.setOnLongClickListener {

            // Aquí se reintroduce la lógica del AlertDialog de eliminación que tenías
            // en el primer ejemplo, adaptada al modelo 'hechizo'.
            android.app.AlertDialog.Builder(context)
                .setTitle("Eliminar Hechizo")
                .setMessage("¿Estás seguro de que deseas eliminar el hechizo: ${hechizo.nombre}?")
                .setPositiveButton("Sí") { dialog, _ ->
                    // ⚠️ IMPORTANTE: Necesitas una referencia al ViewModel aquí para llamar a la eliminación.
                    // Asumiendo que tienes una propiedad 'viewModel' accesible en tu Adapter:
                    // viewModel.eliminarHechizo(hechizo.id)

                    Toast.makeText(context, "Hechizo '${hechizo.nombre}' eliminado (simulación)", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            true // Indica que el evento fue consumido
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