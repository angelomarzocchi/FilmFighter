package com.example.filmfighter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filmfighter.databinding.FighterListItemBinding
import com.example.filmfighter.model.Fighter

class FighterListAdapter(): ListAdapter<Fighter,
        FighterListAdapter.FighterViewHolder>(DiffCallback) {

            class FighterViewHolder(
                private var binding:
                FighterListItemBinding
            ): RecyclerView.ViewHolder(binding.root) {
                fun bind(fighter: Fighter){
                    binding.fighter = fighter
                    binding.executePendingBindings()
                }
            }

    companion object DiffCallback: DiffUtil.ItemCallback<Fighter>() {
        override fun areItemsTheSame(oldItem: Fighter, newItem: Fighter): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Fighter, newItem: Fighter): Boolean {
            return oldItem.name == newItem.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FighterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FighterViewHolder(
            FighterListItemBinding.inflate(layoutInflater,parent,false)
        )
    }

    override fun onBindViewHolder(holder: FighterViewHolder, position: Int) {
        val fighter = getItem(position)
        holder.bind(fighter)
    }


}