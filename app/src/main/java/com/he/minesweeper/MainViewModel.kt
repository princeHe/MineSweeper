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
        return (0 until difficultyMode.rowCount * difficultyMode.columnCount).map {
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
            Grid(index = it, isLandmine = it in landmines, landCountAround = aroundCount)
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
        val rowIndex = index / difficultyMode.columnCount
        val columnIndex = index % difficultyMode.columnCount
        val list = mutableListOf<Grid>()
        //top left
        if (rowIndex > 0 && columnIndex > 0) {
            val currentIndex = index - difficultyMode.columnCount - 1
            list.add(grids.value!![currentIndex])
        }
        //top
        if (rowIndex > 0) {
            val currentIndex = index - difficultyMode.columnCount
            list.add(grids.value!![currentIndex])
        }
        //top right
        if (rowIndex > 0 && columnIndex + 1 < difficultyMode.columnCount) {
            val currentIndex = index - difficultyMode.columnCount + 1
            list.add(grids.value!![currentIndex])
        }
        //left
        if (columnIndex > 0) {
            val currentIndex = index - 1
            list.add(grids.value!![currentIndex])
        }
        //right
        if (columnIndex + 1 < difficultyMode.columnCount) {
            val currentIndex = index + 1
            list.add(grids.value!![currentIndex])
        }
        //bottom left
        if (rowIndex + 1 < difficultyMode.rowCount && columnIndex > 0) {
            val currentIndex = index + difficultyMode.columnCount - 1
            list.add(grids.value!![currentIndex])
        }
        //bottom
        if (rowIndex + 1 < difficultyMode.rowCount) {
            val currentIndex = index + difficultyMode.columnCount
            list.add(grids.value!![currentIndex])
        }
        //bottom right
        if (rowIndex + 1 < difficultyMode.rowCount && columnIndex + 1 < difficultyMode.columnCount) {
            val currentIndex = index + difficultyMode.columnCount + 1
            list.add(grids.value!![currentIndex])
        }
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