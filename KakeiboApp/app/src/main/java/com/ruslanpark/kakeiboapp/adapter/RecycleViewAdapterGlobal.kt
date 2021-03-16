package com.ruslanpark.kakeiboapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruslanpark.kakeiboapp.databinding.ItemBinding
import com.ruslanpark.kakeiboapp.databinding.ItemGlobalBinding

class RecycleViewAdapterGlobal (private var counts : ArrayList< Pair<String, Int> >) :
    RecyclerView.Adapter<RecycleViewAdapterGlobal.ValueViewHolder>() {

    class ValueViewHolder(val binding : ItemGlobalBinding) : RecyclerView.ViewHolder(binding.root) {
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
        return ValueViewHolder(ItemGlobalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ValueViewHolder, position: Int) = holder.bind(counts[position])

    override fun getItemCount() = counts.size

    fun update(listOfPurchases : ArrayList< Pair<String, Int> >) {
        counts = listOfPurchases
        notifyDataSetChanged()
    }
}