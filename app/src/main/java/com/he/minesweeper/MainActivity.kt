package com.he.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.he.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private val dialog = OperateDialog.newInstance({
        viewModel.clear(it)
    }, {
        viewModel.flag(it)
    }, {
        viewModel.doubt(it)
    })

    private val adapter = GridAdapter({ index ->
        if (viewModel.grids.value!![index].isLandMine) {
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
        binding.recyclerView.layoutManager = GridLayoutManager(this, 16)

        viewModel.grids.observe(this) {
            adapter.submitList(it)
        }
    }

}