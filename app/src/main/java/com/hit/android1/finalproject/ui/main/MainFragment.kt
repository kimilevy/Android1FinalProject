package com.hit.android1.finalproject.ui.main
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Extensions.openSnackbar
import com.hit.android1.finalproject.app.Extensions.openTopSnackbar
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import com.hit.android1.finalproject.ui.adapters.InventoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : AppFragmentWithModel<MainFragmentBinding, MainViewModel>(MainViewModel::class.java) {
    override fun inflate() = MainFragmentBinding.inflate(layoutInflater)

    override fun runOnCreateView(view: View) {
        viewModel.cool = "UNCOOL ANYMORE 🎨"
        initializeViewModel()
        initializeAlchemyPlaygroundObservable()
        initializeClearButton()
    }

    private fun initializeClearButton() {
        binding.clearButton.setOnClickListener { binding.alchemyPlayground.clear() }
    }

    private fun initializeAlchemyPlaygroundObservable() {
        binding.alchemyPlayground.setOnItemMerge {
            openSnackbar(it)
        }
    }

    private suspend fun initializeRecycler() {
        withContext(Dispatchers.Main) {
            binding.itemInventoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
            binding.itemInventoryRecyclerView.adapter = InventoryAdapter(viewModel.items!!)
        }
    }

    private fun initializeViewModel() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.items = dao.getItems()
            viewModel.unlockedItems = dao.getUnlockedItems()
            initializeRecycler()
        }
    }
}