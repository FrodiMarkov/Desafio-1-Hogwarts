package com.example.desafio1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio1.databinding.ItemRankingCasasBinding
import model.Casa

class CasaAdapter(private var casas: List<Casa>) :
    RecyclerView.Adapter<CasaAdapter.CasaViewHolder>() {

    class CasaViewHolder(val binding: ItemRankingCasasBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CasaViewHolder {
        val binding = ItemRankingCasasBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CasaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CasaViewHolder, position: Int) {
        val casa = casas[position]

        holder.binding.textRankingPosition.text = "${position + 1}."
        holder.binding.textHouseName.text = casa.nombreCasa
        holder.binding.textHousePoints.text = "${casa.puntosCasa} Pts"
    }

    override fun getItemCount() = casas.size

    fun updateData(newCasas: List<Casa>) {
        casas = newCasas
        notifyDataSetChanged()
    }
}