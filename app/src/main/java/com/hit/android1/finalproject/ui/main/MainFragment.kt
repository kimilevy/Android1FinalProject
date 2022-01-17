package com.hit.android1.finalproject.ui.main
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Extensions.openSnackbar
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import com.hit.android1.finalproject.ui.adapters.InventoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : AppFragmentWithModel<MainFragmentBinding, MainViewModel>(MainViewModel::class.java) {
    override fun inflate() = MainFragmentBinding.inflate(layoutInflater)
    lateinit var recyclerAdapter: InventoryAdapter

    override fun runOnCreateView(view: View) {
        viewModel.cool = "UNCOOL ANYMORE 🎨"
        recyclerAdapter = InventoryAdapter()
        initializeViewModel()
        initializeAlchemyPlaygroundObservable()
        initializeClearButton()
        initializeTopBar()
    }

    private fun initializeClearButton() {
        binding.clearButton.setOnClickListener { binding.alchemyPlayground.clear() }
    }

    private fun initializeAlchemyPlaygroundObservable() {
        binding.alchemyPlayground.setOnItemMerge {
            openSnackbar(it)
            GlobalScope.launch(Dispatchers.IO) {
                dao.unlockItem(it.id)
                withContext(Dispatchers.Main) {
                    recyclerAdapter.unlockItem(it)
                }
            }
        }
    }

    private fun initializeRecycler() {
        binding.itemInventoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        binding.itemInventoryRecyclerView.adapter = recyclerAdapter
    }

    private fun initializeViewModel() {
        initializeRecycler()
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.items = dao.getItems()
            viewModel.unlockedItems = dao.getUnlockedItems()
            withContext(Dispatchers.Main) {
                recyclerAdapter.inventory = viewModel.unlockedItems!!.toMutableList()
            }
        }
    }

    private fun initializeTopBar() {
        binding.elementsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_elementsFragment)
        }
    }
}