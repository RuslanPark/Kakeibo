package com.ruslanpark.kakeiboapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruslanpark.kakeiboapp.databinding.ItemBinding

class RecycleViewAdapter (private var counts : MutableList<Int>, val onClickDeleteButton : (Int) -> Unit) :
    RecyclerView.Adapter<RecycleViewAdapter.ValueViewHolder>() {

    class ValueViewHolder(val binding : ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(number : Int) {
            binding.textView.text = number.toString()
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

    fun update(listOfPosts : MutableList<Int>) {
        counts = listOfPosts
        notifyDataSetChanged()
    }

    fun delete(position: Int) {
        counts.removeAt(position)
        notifyDataSetChanged()
    }

}