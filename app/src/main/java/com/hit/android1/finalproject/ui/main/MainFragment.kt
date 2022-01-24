package com.hit.android1.finalproject.ui.main
import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.AppFragmentWithModel
import com.hit.android1.finalproject.app.Extensions.openSnackbar
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.app.Globals.sfxPlayer
import com.hit.android1.finalproject.dao.entities.InventoryItem.Companion.name
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import com.hit.android1.finalproject.ui.adapters.InventoryAdapter
import com.hit.android1.finalproject.ui.customviews.ItemView
import com.hit.android1.finalproject.ui.sharedmodels.SharedModel
import kotlinx.coroutines.*

class MainFragment : AppFragmentWithModel<MainFragmentBinding, SharedModel>(SharedModel::class.java) {
    companion object {
        val ONBOARDING_FINISHED_SP_KEY = "ONBOARDING_FINISHED"
    }

    override fun inflate() = MainFragmentBinding.inflate(layoutInflater)
    lateinit var recyclerAdapter: InventoryAdapter
    var textDebounceJob: Job? = null

    override fun runOnCreateView(view: View) {
        recyclerAdapter = InventoryAdapter(requireContext())
        initializeViewModel()
        initializeAlchemyPlaygroundObservable()
        initializeClearButton()
        initializeTopBar()
        initializeSearchBar()
    }

    private fun startOnboarding() {
        onboardingStep1()
    }

    private fun initializeClearButton() {
        binding.clearButton.setOnClickListener {
            binding.alchemyPlayground.clear()
            sfxPlayer?.play(R.raw.clean)
        }
    }

    private fun initializeAlchemyPlaygroundObservable() {
        binding.alchemyPlayground.setOnItemMerge {
            openSnackbar(it)
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

    // On boarding steps:
    private fun onboardingStep1() {
        val layoutManager = binding.itemInventoryRecyclerView.layoutManager as GridLayoutManager

        val firstItem = layoutManager.findViewByPosition(0) as ItemView
        val firstItemDraggableLayout = firstItem.draggableLayout

        TapTargetView.showFor(
            requireActivity(),
            TapTarget
                .forView(firstItemDraggableLayout, getString(R.string.onboarding_first_item_hint))
                .transparentTarget(true)
                .cancelable(false),
            object : TapTargetView.Listener() {
                override fun onTargetLongClick(view: TapTargetView?) {
                    this.onTargetClick(view)
                    firstItemDraggableLayout.performLongClick()
                    binding.alchemyPlayground.addOnceOnDropListener {
                        onboardingStep2()
                    }
                }
            }
        )
    }

    private fun onboardingStep2() {
        val layoutManager = binding.itemInventoryRecyclerView.layoutManager as GridLayoutManager

        val secondItem = layoutManager.findViewByPosition(1) as ItemView
        val secondItemDraggableLayout = secondItem.draggableLayout

        TapTargetView.showFor(
            requireActivity(),
            TapTarget
                .forView(secondItemDraggableLayout, getString(R.string.onboarding_second_item_hint))
                .transparentTarget(true)
                .cancelable(false),
            object : TapTargetView.Listener() {
                override fun onTargetLongClick(view: TapTargetView?) {
                    this.onTargetClick(view)
                    secondItemDraggableLayout.performLongClick()
                    binding.alchemyPlayground.addOnceOnDropListener {
                        onboardingStep3()
                    }
                }
            }
        )

    }

    private fun onboardingStep3() {
        TapTargetView.showFor(
            requireActivity(),
            TapTarget
                .forView(binding.elementsButton, getString(R.string.onboarding_elments_button))
                .transparentTarget(true)
                .cancelable(false),
            object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView?) {
                    super.onTargetClick(view)
                    onboardingStep4()
                }
            }
        )
    }

    private fun onboardingStep4() {
        TapTargetView.showFor(
            requireActivity(),
            TapTarget
                .forView(binding.clearButton, getString(R.string.onboarding_clear_button))
                .transparentTarget(true)
                .cancelable(false),
            object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView?) {
                    super.onTargetClick(view)
                    onboardingStep5()
                }
            }
        )
    }

    private fun onboardingStep5() {

        TapTargetView.showFor(
            requireActivity(),
            TapTarget
                .forView(binding.onboardingOkButton, getString(R.string.onboarding_thats_it))
                .transparentTarget(true)
                .cancelable(false),
            object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView?) {
                    super.onTargetClick(view)
                    binding.onboardingOkButton.visibility = View.GONE
                    onboardingFinished()
                }
            }
        )

        binding.onboardingOkButton.visibility = View.VISIBLE
    }

    private fun onboardingFinished() {
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        sharedPreferences
            .edit()
            .putBoolean(ONBOARDING_FINISHED_SP_KEY, true)
            .apply()
    }


}