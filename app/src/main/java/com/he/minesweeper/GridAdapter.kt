package com.he.minesweeper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.he.minesweeper.databinding.ItemGridBinding

class GridAdapter(
    private val clickListener: (Int) -> Unit, private val longClickListener: (Int) -> Unit
) : ListAdapter<Grid, GridAdapter.ViewHolder>(Grid.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        clickListener, longClickListener
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemGridBinding,
        private val clickListener: (Int) -> Unit,
        private val longClickListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Grid) {
            with(binding) {
                root.setOnClickListener {
                    if (item.status == GridStatus.NORMAL) clickListener(adapterPosition)
                }
                root.setOnLongClickListener {
                    if (item.status != GridStatus.CONFIRM) longClickListener(adapterPosition)
                    true
                }
                textView.text = if (
                    item.status == GridStatus.CONFIRM && !item.isLandmine && item.landCountAround != 0
                ) item.landCountAround.toString() else ""
                appCompatImageView.setImageResource(
                    when (item.status) {
                        GridStatus.NORMAL -> 0
                        GridStatus.CONFIRM -> if (item.isLandmine) R.drawable.ic_landmine else 0
                        GridStatus.FLAG -> R.drawable.ic_flag
                        GridStatus.DOUBT -> R.drawable.ic_doubt
                    }
                )
                appCompatImageView.setBackgroundResource(
                    when (item.status) {
                        GridStatus.NORMAL, GridStatus.FLAG, GridStatus.DOUBT -> R.color.selector_bg_color
                        GridStatus.CONFIRM -> R.color.purple_200
                    }
                )
            }
        }

    }
}