package com.hit.android1.finalproject.ui.elements

import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.databinding.ElementsFragmentBinding
import com.hit.android1.finalproject.ui.adapters.InventoryAdapter
import com.hit.android1.finalproject.ui.main.MainViewModel


class ElementsFragment : AppFragmentWithModel<ElementsFragmentBinding, MainViewModel>(MainViewModel::class.java) {
    override fun inflate() = ElementsFragmentBinding.inflate(layoutInflater)
    lateinit var inventoryAdapter: InventoryAdapter

    override fun runOnCreateView(view: View) {
        inventoryAdapter = InventoryAdapter()
        binding.itemInventoryRecyclerView.adapter = inventoryAdapter
        viewModel.items?.let {
            inventoryAdapter.inventory = it.toMutableList()
            binding.amountAchievedText.text = getString(R.string.amount_unlocked_elements, viewModel.unlockedItems?.size, it.size)
        }
        binding.backButton.setOnClickListener {
            findNavController().apply {
                navigate(R.id.action_ElementsFragment_to_mainFragment)
            }
        }
    }
}