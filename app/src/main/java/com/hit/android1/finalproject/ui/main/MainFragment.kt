package com.hit.android1.finalproject.ui.main
import android.view.View
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.dao.entities.InventoryItem.Companion.drawableResourceId
import com.hit.android1.finalproject.dao.entities.InventoryItem.Companion.name
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : AppFragmentWithModel<MainFragmentBinding, MainViewModel>(MainViewModel::class.java) {
    override fun inflate() = MainFragmentBinding.inflate(layoutInflater)

    override fun runOnCreateView(view: View) {
        binding.coolButton.text = viewModel.cool
        viewModel.cool = "UNCOOL ANYMORE 🎨"
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.items = dao.getItems()
        }
        binding.coolButton.setOnClickListener {
//            findNavController().navigate(R.id.secondFragment)
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.getRandomItem()?.let {
//                    val itemResourceId = it.drawableResourceId(requireContext())
                    withContext(Dispatchers.Main) {
                        binding.testImage.setImageResource(R.drawable.inventory_item_science)
                        binding.itemTitle.text = it.name()
                    }
                }
            }
        }
    }
}