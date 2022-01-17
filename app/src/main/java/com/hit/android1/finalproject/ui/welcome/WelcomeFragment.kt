package com.hit.android1.finalproject.ui.welcome

import android.view.View
import androidx.navigation.fragment.findNavController
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragment
import com.hit.android1.finalproject.databinding.WelcomeFragmentBinding

class WelcomeFragment : AppFragment<WelcomeFragmentBinding>() {
    override fun inflate() = WelcomeFragmentBinding.inflate(layoutInflater)

    override fun runOnCreateView(view: View) {
        binding.playButtonGame.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)
        }

    }


}