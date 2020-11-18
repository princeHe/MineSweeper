package com.he.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.he.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private val dialog = OperateDialog.newInstance({
        viewModel.confirm()
    }, {
        viewModel.flag()
    }, {
        viewModel.question()
    })

    private val adapter = GridAdapter { index ->
        viewModel.index = index
        dialog.show(supportFragmentManager, "operate")
    }

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