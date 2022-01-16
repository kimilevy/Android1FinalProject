package com.hit.android1.finalproject.ui.welcome

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragment
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Globals
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import com.hit.android1.finalproject.databinding.WelcomeFragmentBinding
import com.hit.android1.finalproject.ui.main.InventoryAdapter
import com.hit.android1.finalproject.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeFragment : AppFragment<WelcomeFragmentBinding>() {
    override fun inflate() = WelcomeFragmentBinding.inflate(layoutInflater)

    override fun runOnCreateView(view: View) {
        binding.playButtonGame.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

    }


}