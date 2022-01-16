package com.hit.android1.finalproject.ui.welcome

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Globals
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import com.hit.android1.finalproject.ui.main.InventoryAdapter
import com.hit.android1.finalproject.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeFragment : AppFragmentWithModel<MainFragmentBinding, MainViewModel>(MainViewModel::class.java) {
    override fun inflate() = MainFragmentBinding.inflate(layoutInflater)

    override fun runOnCreateView(view: View) {
        viewModel.cool = "UNCOOL ANYMORE 🎨"
        initializeViewModel()
    }

    private suspend fun initializeRecycler() {
        withContext(Dispatchers.Main) {
            binding.itemInventoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
            binding.itemInventoryRecyclerView.adapter = InventoryAdapter(viewModel.items!!)
        }
    }

    private fun initializeViewModel() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.items = Globals.dao.getItems()
            viewModel.unlockedItems = Globals.dao.getUnlockedItems()
            initializeRecycler()
        }
    }
}