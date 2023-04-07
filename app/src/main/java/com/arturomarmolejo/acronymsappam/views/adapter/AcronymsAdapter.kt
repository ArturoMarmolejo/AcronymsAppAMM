package com.arturomarmolejo.acronymsappam.views.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arturomarmolejo.acronymsappam.databinding.AcronymCardItemBinding
import com.arturomarmolejo.acronymsappam.model.AcromineItem
import com.arturomarmolejo.acronymsappam.model.Lf

private const val TAG = "AcronymsAdapter"
class AcronymsAdapter(
    private val itemSet: MutableList<AcromineItem> = mutableListOf(),
    private val onItemClick: (acronymItem: Lf) -> Unit
): RecyclerView.Adapter<AcronymsViewHolder>() {
    fun updateItems(newItems: MutableList<AcromineItem>) {
        if(itemSet != newItems) {
            itemSet.clear()
            itemSet.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcronymsViewHolder =
        AcronymsViewHolder(
            AcronymCardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AcronymsViewHolder, position: Int) =
        holder.bind(itemSet.first().lfs[position], onItemClick)

    override fun getItemCount(): Int = if(itemSet.isNotEmpty()) itemSet.first().lfs.size else 0
}

class AcronymsViewHolder(
    private val binding: AcronymCardItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Lf, onItemClick: (previewLongForm: Lf) -> Unit) {
        binding.acronymMeaning.text = item.lf

        itemView.setOnClickListener {
            onItemClick(item)
        }

    }
}