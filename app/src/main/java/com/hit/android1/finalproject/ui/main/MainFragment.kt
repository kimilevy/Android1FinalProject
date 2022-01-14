package com.hit.android1.finalproject.ui.main
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hit.android1.finalproject.Extensions.logDebug
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.databinding.MainFragmentBinding

class MainFragment : AppFragmentWithModel<MainFragmentBinding, MainViewModel>(MainViewModel::class.java) {
    override fun inflate() = MainFragmentBinding.inflate(layoutInflater)

    override fun runOnCreateView(view: View) {
        binding.coolButton.text = viewModel.cool
        viewModel.cool = "UNCOOL ANYMORE 🎨"
        binding.coolButton.setOnClickListener {
            findNavController().navigate(R.id.secondFragment)
        }
    }
}