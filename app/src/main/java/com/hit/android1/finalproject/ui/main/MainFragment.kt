package com.hit.android1.finalproject.ui.main
import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.app.Globals.sfxPlayer
import com.hit.android1.finalproject.app.OnBoardingHelper
import com.hit.android1.finalproject.dao.entities.InventoryItem.Companion.name
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import com.hit.android1.finalproject.ui.adapters.InventoryAdapter
import com.hit.android1.finalproject.ui.sharedmodels.SharedModel
import kotlinx.coroutines.*

class MainFragment : AppFragmentWithModel<MainFragmentBinding, SharedModel>(SharedModel::class.java) {
    companion object {
        const val ONBOARDING_FINISHED_SP_KEY = "ONBOARDING_FINISHED"
    }

    override fun inflate() = MainFragmentBinding.inflate(layoutInflater)
    private lateinit var recyclerAdapter: InventoryAdapter
    private var textDebounceJob: Job? = null

    override fun runOnCreateView(view: View) {
        recyclerAdapter = InventoryAdapter(requireContext())
        initializeViewModel()
        initializeAlchemyPlaygroundObservable()
        initializeClearButton()
        initializeTopBar()
        initializeSearchBar()
    }

    private fun startOnboarding() {
        OnBoardingHelper.start(binding, requireActivity())
    }

    private fun initializeClearButton() {
        binding.clearButton.setOnClickListener {
            binding.alchemyPlayground.clear()
            sfxPlayer?.play(R.raw.clean)
        }
    }

    private fun initializeAlchemyPlaygroundObservable() {
        binding.alchemyPlayground.setOnItemMerge {
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.unlockItem(it)
                withContext(Dispatchers.Main) {
                    recyclerAdapter.unlockItem(it)
                }
            }
        }
    }

    private fun initializeRecycler() {
        binding.itemInventoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        binding.itemInventoryRecyclerView.adapter = recyclerAdapter
        viewModel.addUnlockedListener {
            withContext(Dispatchers.Main) {
                recyclerAdapter.unlockItem(it)
            }
        }
    }

    private fun initializeViewModel() {
        initializeRecycler()
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.items = dao.getItems().sortedBy {
                i -> i.name(requireContext())
            }
            viewModel.unlockedItems = dao.getUnlockedItems().toMutableList()
            withContext(Dispatchers.Main) {
                recyclerAdapter.inventory = viewModel.unlockedItems!!.toMutableList()
                binding.itemInventoryRecyclerView.post {
                    // This must come after initializing the recycler adapter
                    if (!requireActivity().getPreferences(Context.MODE_PRIVATE).contains(ONBOARDING_FINISHED_SP_KEY)) {
                        startOnboarding()
                    }
                }
            }


        }
    }

    private fun initializeTopBar() {
        binding.elementsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_elementsFragment)
        }
    }

    private fun initializeSearchBar() {
        binding.searchBar.editText?.doOnTextChanged { inputText, _, _, _ ->
            GlobalScope.launch {
                // if the last launched job is not finished, remove it and retry (Debounce)
                textDebounceJob?.cancelAndJoin()
                textDebounceJob = GlobalScope.launch {
                    delay(300)
                    withContext(Dispatchers.Main) {
                        recyclerAdapter.filter.filter(inputText)
                    }
                }
            }

        }
    }

}