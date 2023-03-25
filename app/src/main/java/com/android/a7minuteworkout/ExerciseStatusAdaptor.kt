package com.android.a7minuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.a7minuteworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdaptor(val items: List<ExerciseModel>) :
    RecyclerView.Adapter<ExerciseStatusAdaptor.ViewHolder>() {

    inner class ViewHolder(binding: ItemExerciseStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item = binding.tvItem

        fun bindItem(exerciseModel: ExerciseModel) {
            item.text = exerciseModel.id.toString()
            when {
                exerciseModel.isSelected -> {

                    item.background = ContextCompat.getDrawable(
                        this.itemView.context,
                        R.drawable.item_circular_color_white_background
                    )
                    item.setTextColor(Color.parseColor("#212121"))
                }
                exerciseModel.isCompleted -> {
                    item.background = ContextCompat.getDrawable(
                        this.itemView.context,
                        R.drawable.item_circular_color_accent_background
                    )
                    item.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    item.background = ContextCompat.getDrawable(
                        this.itemView.context,
                        R.drawable.item_circular_color_gray_background
                    )
                    item.setTextColor(Color.parseColor("#212121"))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExerciseStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exerciseModel = items[position]
        holder.bindItem(exerciseModel)
    }
}