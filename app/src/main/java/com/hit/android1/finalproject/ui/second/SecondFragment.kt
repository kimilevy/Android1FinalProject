package com.hit.android1.finalproject.ui.second

import android.view.View
import androidx.navigation.fragment.findNavController
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.databinding.FragmentSecondBinding


class SecondFragment : AppFragmentWithModel<FragmentSecondBinding, SecondViewModel>(SecondViewModel::class.java) {
    override fun inflate() = FragmentSecondBinding.inflate(layoutInflater)

    override fun runOnCreateView(view: View) {
        binding.button.text = viewModel.cool
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
    }
}