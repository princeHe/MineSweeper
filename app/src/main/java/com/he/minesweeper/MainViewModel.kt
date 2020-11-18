package com.he.minesweeper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel : ViewModel() {

    companion object {
        const val rowCount = 30
        const val columnCount = 16
        const val landmineCount = 99
    }

    private val landmines by lazy {
        val result = mutableSetOf<Int>()
        do {
            result.add(Random.nextInt(rowCount * columnCount))
        } while (result.size < landmineCount)
        result
    }

    val grids = MutableLiveData(
        (0 until rowCount * columnCount).map {
            val rowIndex = it / columnCount
            val columnIndex = it % columnCount
            var aroundCount = 0
            //top left
            if (rowIndex > 0 && columnIndex > 0 && it - columnCount - 1 in landmines) ++aroundCount
            //top
            if (rowIndex > 0 && it - columnCount in landmines) ++aroundCount
            //top right
            if (rowIndex > 0 && columnIndex + 1 < columnCount && it - columnCount + 1 in landmines) ++aroundCount
            //left
            if (columnIndex > 0 && it - 1 in landmines) ++aroundCount
            //right
            if (columnIndex + 1 < columnCount && it + 1 in landmines) ++aroundCount
            //bottom left
            if (rowIndex + 1 < rowCount && columnIndex > 0 && it + columnCount - 1 in landmines) ++aroundCount
            //bottom
            if (rowIndex + 1 < rowCount && it + columnCount in landmines) ++aroundCount
            //bottom right
            if (rowIndex + 1 < rowCount && columnIndex + 1 < columnCount && it + columnCount + 1 in landmines) ++aroundCount
            Grid(index = it, isLandMine = it in landmines, landCountAround = aroundCount)
        }
    )

    fun reset() {

    }

    fun confirm(index: Int) {
        //确定扫除一个的时候需要自动
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

}