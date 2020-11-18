package com.he.minesweeper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.he.minesweeper.databinding.ItemGridBinding

class GridAdapter(private val listener: (Int) -> Unit)
    : ListAdapter<Grid, GridViewHolder>(Grid.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GridViewHolder(
        ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        listener
    )

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class GridViewHolder(
    private val binding: ItemGridBinding,
    listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener { listener(adapterPosition) }
    }

    fun bind(item: Grid) {
        with(binding) {
            textView.text = if (!item.isLandMine && item.landCountAround != 0)
                item.landCountAround.toString() else ""
            appCompatImageView.setImageResource(if (item.isLandMine) R.drawable.ic_landmine else 0)
            appCompatImageView.setBackgroundResource(if (item.isClickOn) R.color.purple_200 else R.color.purple_700)
        }
    }

}