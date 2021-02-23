package com.ruslanpark.kakeiboapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruslanpark.kakeiboapp.databinding.ItemBinding

class RecycleViewAdapter (private var counts : ArrayList< Pair<String, Int> >, val onClickDeleteButton : (Int) -> Unit) :
    RecyclerView.Adapter<RecycleViewAdapter.ValueViewHolder>() {

    class ValueViewHolder(val binding : ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(number : Pair<String, Int>) {
            binding.textViewDesc.text = number.first
            binding.textView.text = number.second.toString()
            if (number.second < 0) {
                binding.textView.setTextColor(Color.RED)
            } else {
                binding.textView.setTextColor(Color.GREEN)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValueViewHolder {
        val holder = ValueViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        holder.binding.deleteButton.setOnClickListener {
            onClickDeleteButton(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ValueViewHolder, position: Int) = holder.bind(counts[position])

    override fun getItemCount() = counts.size

    fun update(listOfPurchases : ArrayList< Pair<String, Int> >) {
        counts = listOfPurchases
        notifyDataSetChanged()
    }
}