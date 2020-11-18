package com.he.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.he.minesweeper.databinding.DialogOperationBinding

class OperateDialog(
    private val confirmListener: () -> Unit,
    private val flagListener: () -> Unit,
    private val questionListener: () -> Unit
) : DialogFragment() {

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
            tvConfirm.setOnClickListener {
                confirmListener()
                dismissAllowingStateLoss()
            }
            tvFlag.setOnClickListener {
                flagListener()
                dismissAllowingStateLoss()
            }
            tvQuestion.setOnClickListener {
                questionListener()
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        fun newInstance(
            confirmListener: () -> Unit, flagListener: () -> Unit, questionListener: () -> Unit
        ) = OperateDialog(confirmListener, flagListener, questionListener)
    }

}