package com.he.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.he.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(DifficultyMode.HARD) as T
            }
        }
    }

    private val dialog = OperateDialog.newInstance({
        viewModel.clear(it)
    }, {
        viewModel.flag(it)
    }, {
        viewModel.doubt(it)
    })

    private val adapter = GridAdapter({ index ->
        if (viewModel.grids.value!![index].isLandmine) {
            AlertDialog.Builder(this)
                .setMessage("祝你下次好运")
                .setPositiveButton("再来一局") { _, _ ->
                    viewModel.reset()
                }
                .setNegativeButton("退出") { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .create()
                .show()
        }
        viewModel.confirm(index)
    }, { index ->
        dialog.index = index
        dialog.show(supportFragmentManager, "operate")
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, viewModel.difficultyMode.columnCount)

        viewModel.grids.observe(this) {
            adapter.submitList(it)
            if (viewModel.success) {
                AlertDialog.Builder(this)
                    .setMessage("恭喜您！")
                    .setPositiveButton("再来一局") { _, _ ->
                        viewModel.reset()
                    }
                    .setNegativeButton("退出") { _, _ ->
                        finish()
                    }
                    .setCancelable(false)
                    .create()
                    .show()
            }
        }
    }

}