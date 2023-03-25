package com.android.a7minuteworkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.a7minuteworkout.R
import com.android.a7minuteworkout.databinding.HistoryRowBinding
import com.android.roomdemo.HistoryEntity

class HistoryAdapter(
    private val items: ArrayList<HistoryEntity>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: HistoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvExerciseNumber = binding.tvExerciseNumber
        val tvExerciseDate = binding.tvExerciseDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HistoryRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvExerciseNumber.text = item.id.toString()
        holder.tvExerciseDate.text = item.date

        val color: Int?
        if (position % 2 == 0) {
            color = R.color.Gainsboro
        } else {
            color = R.color.White
        }

        holder.llMain.setBackgroundColor(ContextCompat.getColor(context, color))
    }
}