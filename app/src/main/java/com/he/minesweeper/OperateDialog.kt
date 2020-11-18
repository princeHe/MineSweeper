package com.he.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.he.minesweeper.databinding.DialogOperationBinding

class OperateDialog(
    private val clearListener: (Int) -> Unit,
    private val flagListener: (Int) -> Unit,
    private val doubtListener: (Int) -> Unit
) : DialogFragment() {

    var index = 0

    private lateinit var binding: DialogOperationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvClear.setOnClickListener {
                clearListener(index)
                dismissAllowingStateLoss()
            }
            tvFlag.setOnClickListener {
                flagListener(index)
                dismissAllowingStateLoss()
            }
            tvDoubt.setOnClickListener {
                doubtListener(index)
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        fun newInstance(
            clearListener: (Int) -> Unit, flagListener: (Int) -> Unit, doubtListener: (Int) -> Unit
        ) = OperateDialog(clearListener, flagListener, doubtListener)
    }

}