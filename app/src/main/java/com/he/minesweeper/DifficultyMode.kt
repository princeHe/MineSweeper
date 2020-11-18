package com.he.minesweeper

enum class DifficultyMode(val rowCount: Int, val columnCount: Int, val landmineCount: Int) {
    EASY(9,9, 16),
    MEDIUM(16, 16, 36),
    HARD(30, 16, 81)
}