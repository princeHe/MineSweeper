package com.he.minesweeper

import androidx.recyclerview.widget.DiffUtil

data class Grid(
        val index: Int,
        val landCountAround: Int = 0,
        val isLandMine: Boolean = false,
        val status: GridStatus = GridStatus.NORMAL
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Grid>() {
            override fun areItemsTheSame(oldItem: Grid, newItem: Grid): Boolean {
                return oldItem.index == newItem.index
            }

            override fun areContentsTheSame(oldItem: Grid, newItem: Grid): Boolean {
                return oldItem == newItem
            }
        }
    }
}