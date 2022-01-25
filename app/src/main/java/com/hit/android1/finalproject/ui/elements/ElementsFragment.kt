package com.hit.android1.finalproject.ui.elements

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.databinding.ElementsFragmentBinding
import com.hit.android1.finalproject.models.ItemDialogData
import com.hit.android1.finalproject.ui.adapters.InventoryAdapter
import com.hit.android1.finalproject.ui.customviews.ItemDialogFragment
import com.hit.android1.finalproject.ui.sharedmodels.SharedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ElementsFragment : AppFragmentWithModel<ElementsFragmentBinding, SharedModel>(SharedModel::class.java) {
    override fun inflate() = ElementsFragmentBinding.inflate(layoutInflater)
    private lateinit var inventoryAdapter: InventoryAdapter

    override fun runOnCreateView(view: View) {
        inventoryAdapter = InventoryAdapter(requireContext(), false) {
            it?.let { item ->
                if (!item.unlocked) return@InventoryAdapter
                GlobalScope.launch(Dispatchers.IO) {
                    val bundle = Bundle()
                    val itemWithRecipes = dao.getItemWithRecipes(item.id)
                    bundle.putString("DATA", Json.encodeToString(ItemDialogData(itemWithRecipes)))
                    val fragment = ItemDialogFragment()
                    fragment.arguments = bundle
                    fragment.show(childFragmentManager, ItemDialogFragment.TAG)
                }
            }
        }
        binding.itemInventoryRecyclerView.adapter = inventoryAdapter

        viewModel.items?.let {
            inventoryAdapter.inventory = it.toMutableList()
            binding.amountAchievedText.text = getString(R.string.amount_unlocked_elements, viewModel.unlockedItems?.size, it.size)
            inventoryAdapter.inventory = viewModel.items!!.toMutableList()

            viewModel.addUnlockedListener { item ->
                withContext(Dispatchers.Main) {
                    inventoryAdapter.unlockItem(item, false)
                }
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().apply {
                navigate(R.id.action_ElementsFragment_to_mainFragment)
            }
        }
    }
}