package com.he.minesweeper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel(val difficultyMode: DifficultyMode) : ViewModel() {

    val grids = MutableLiveData(generateGrids())

    private fun generateGrids(): List<Grid> {
        val landmines = mutableSetOf<Int>()
        do {
            landmines.add(Random.nextInt(difficultyMode.rowCount * difficultyMode.columnCount))
        } while (landmines.size < difficultyMode.landmineCount)
        return (0 until difficultyMode.rowCount * difficultyMode.columnCount).map { index ->
            Grid(index = index, isLandmine = index in landmines, landCountAround = aroundIndexes(index).count { it in landmines })
        }
    }

    val success get() = grids.value!!.count {
        !it.isLandmine && it.status == GridStatus.CONFIRM
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

    fun findClickableGrids(index: Int): List<Int> {
        val list = grids.value!!.filter { it.index in aroundIndexes(index) }
        if (list.count { it.status == GridStatus.FLAG } == grids.value!![index].landCountAround) {
            return list.filter { it.status == GridStatus.NORMAL }.map { it.index }
        }
        return emptyList()
    }

    //可能是这个小游戏最有意思的一个方法了
    private fun autoScan(index: Int) {
        confirmOne(index)
        if (grids.value!![index].landCountAround != 0) {
            return
        }
        aroundIndexes(index).forEach {
            if (grids.value!![it].status == GridStatus.NORMAL) autoScan(it)
        }
    }

    private fun aroundIndexes(index: Int): List<Int> {
        val list = mutableListOf<Int>()
        val rowIndex = index / difficultyMode.columnCount
        val columnIndex = index % difficultyMode.columnCount
        //top left
        if (rowIndex > 0 && columnIndex > 0) list.add(index - difficultyMode.columnCount - 1 )
        //top
        if (rowIndex > 0) list.add(index - difficultyMode.columnCount)
        //top right
        if (rowIndex > 0 && columnIndex + 1 < difficultyMode.columnCount) list.add(index - difficultyMode.columnCount + 1 )
        //left
        if (columnIndex > 0) list.add(index - 1)
        //right
        if (columnIndex + 1 < difficultyMode.columnCount) list.add(index + 1)
        //bottom left
        if (rowIndex + 1 < difficultyMode.rowCount && columnIndex > 0) list.add(index + difficultyMode.columnCount - 1)
        //bottom
        if (rowIndex + 1 < difficultyMode.rowCount) list.add(index + difficultyMode.columnCount)
        //bottom right
        if (rowIndex + 1 < difficultyMode.rowCount && columnIndex + 1 < difficultyMode.columnCount) list.add(index + difficultyMode.columnCount + 1 )
        return list
    }

}