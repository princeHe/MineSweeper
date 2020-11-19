package com.he.minesweeper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel(private val difficultyMode: DifficultyMode) : ViewModel() {

    private val landmines by lazy {
        val result = mutableSetOf<Int>()
        do {
            result.add(Random.nextInt(difficultyMode.rowCount * difficultyMode.columnCount))
        } while (result.size < difficultyMode.landmineCount)
        result
    }

    val grids = MutableLiveData(generateGrids())

    private fun generateGrids() =
        (0 until difficultyMode.rowCount * difficultyMode.columnCount).map {
            val rowIndex = it / difficultyMode.columnCount
            val columnIndex = it % difficultyMode.columnCount
            var aroundCount = 0
            //top left
            if (rowIndex > 0 && columnIndex > 0 && it - difficultyMode.columnCount - 1 in landmines) ++aroundCount
            //top
            if (rowIndex > 0 && it - difficultyMode.columnCount in landmines) ++aroundCount
            //top right
            if (rowIndex > 0 && columnIndex + 1 < difficultyMode.columnCount && it - difficultyMode.columnCount + 1 in landmines) ++aroundCount
            //left
            if (columnIndex > 0 && it - 1 in landmines) ++aroundCount
            //right
            if (columnIndex + 1 < difficultyMode.columnCount && it + 1 in landmines) ++aroundCount
            //bottom left
            if (rowIndex + 1 < difficultyMode.rowCount && columnIndex > 0 && it + difficultyMode.columnCount - 1 in landmines) ++aroundCount
            //bottom
            if (rowIndex + 1 < difficultyMode.rowCount && it + difficultyMode.columnCount in landmines) ++aroundCount
            //bottom right
            if (rowIndex + 1 < difficultyMode.rowCount && columnIndex + 1 < difficultyMode.columnCount && it + difficultyMode.columnCount + 1 in landmines) ++aroundCount
            Grid(index = it, isLandMine = it in landmines, landCountAround = aroundCount)
        }

    val success get() = grids.value!!.count {
        !it.isLandMine && it.status == GridStatus.CONFIRM
    } == difficultyMode.columnCount * difficultyMode.rowCount - difficultyMode.landmineCount

    fun reset() {
        grids.value = generateGrids()
    }

    fun confirm(index: Int) {
        //确定扫除一个的时候需要自动点开周围雷数量为0的
        if (grids.value!![index].landCountAround == 0) {
            autoScan(index)
        } else {
            confirmOne(index)
        }
    }

    private fun confirmOne(index: Int) {
        grids.value = grids.value!!.toMutableList()
            .also {
                it[index] = it[index].copy(status = GridStatus.CONFIRM)
            }
    }

    fun clear(index: Int) {
        grids.value = grids.value!!.toMutableList()
            .also {
                it[index] = it[index].copy(status = GridStatus.NORMAL)
            }
    }

    fun flag(index: Int) {
        grids.value = grids.value!!.toMutableList()
            .also {
                it[index] = it[index].copy(status = GridStatus.FLAG)
            }
    }

    fun doubt(index: Int) {
        grids.value = grids.value!!.toMutableList()
            .also {
                it[index] = it[index].copy(status = GridStatus.DOUBT)
            }
    }

    //可能是这个小游戏最有意思的一个方法了
    private fun autoScan(index: Int) {
        confirmOne(index)
        if (grids.value!![index].landCountAround != 0) {
            return
        }
        val rowIndex = index / difficultyMode.columnCount
        val columnIndex = index % difficultyMode.columnCount
        //top left
        if (rowIndex > 0 && columnIndex > 0) {
            val currentIndex = index - difficultyMode.columnCount - 1
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
        //top
        if (rowIndex > 0) {
            val currentIndex = index - difficultyMode.columnCount
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
        //top right
        if (rowIndex > 0 && columnIndex + 1 < difficultyMode.columnCount) {
            val currentIndex = index - difficultyMode.columnCount + 1
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
        //left
        if (columnIndex > 0) {
            val currentIndex = index - 1
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
        //right
        if (columnIndex + 1 < difficultyMode.columnCount) {
            val currentIndex = index + 1
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
        //bottom left
        if (rowIndex + 1 < difficultyMode.rowCount && columnIndex > 0) {
            val currentIndex = index + difficultyMode.columnCount - 1
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
        //bottom
        if (rowIndex + 1 < difficultyMode.rowCount) {
            val currentIndex = index + difficultyMode.columnCount
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
        //bottom right
        if (rowIndex + 1 < difficultyMode.rowCount && columnIndex + 1 < difficultyMode.columnCount) {
            val currentIndex = index + difficultyMode.columnCount + 1
            if (grids.value!![currentIndex].status == GridStatus.NORMAL) autoScan(currentIndex)
        }
    }

}