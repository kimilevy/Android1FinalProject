package com.hit.android1.finalproject.app

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.databinding.MainFragmentBinding
import com.hit.android1.finalproject.ui.customviews.ItemView
import com.hit.android1.finalproject.ui.main.MainFragment

object OnBoardingHelper {
    // On boarding steps:
    fun start(binding: MainFragmentBinding, activity: Activity) {
        val layoutManager = binding.itemInventoryRecyclerView.layoutManager as GridLayoutManager

        val firstItem = layoutManager.findViewByPosition(0) as ItemView
        val firstItemDraggableLayout = firstItem.draggableLayout

        TapTargetView.showFor(
            activity,
            TapTarget
                .forView(firstItemDraggableLayout, activity.getString(R.string.onboarding_first_item_hint))
                .transparentTarget(true)
                .cancelable(false),
            object : TapTargetView.Listener() {
                override fun onTargetLongClick(view: TapTargetView?) {
                    this.onTargetClick(view)
                    firstItemDraggableLayout.performLongClick()
                    binding.alchemyPlayground.addOnceOnDropListener {
                        onboardingStep2(binding, activity)
                    }
                }
            }
        )
    }

    private fun onboardingStep2(binding: MainFragmentBinding, activity: Activity) {
        val layoutManager = binding.itemInventoryRecyclerView.layoutManager as GridLayoutManager

        val secondItem = layoutManager.findViewByPosition(1) as ItemView
        val secondItemDraggableLayout = secondItem.draggableLayout

        TapTargetView.showFor(
            activity,
            TapTarget
                .forView(secondItemDraggableLayout, activity.getString(R.string.onboarding_second_item_hint))
                .transparentTarget(true)
                .cancelable(false),
            object : TapTargetView.Listener() {
                override fun onTargetLongClick(view: TapTargetView?) {
                    this.onTargetClick(view)
                    secondItemDraggableLayout.performLongClick()
                    binding.alchemyPlayground.addOnceOnDropListener {
                        onboardingStep3(binding, activity)
                    }
                }
            }
        )

    }

    private fun onboardingStep3(binding: MainFragmentBinding, activity: Activity) {
        TapTargetSequence(activity)
            .targets(
                TapTarget
                    .forView(binding.elementsButton, activity.getString(R.string.onboarding_elments_button))
                    .transparentTarget(true)
                    .id(0)
                    .cancelable(false),
                TapTarget
                    .forView(binding.clearButton, activity.getString(R.string.onboarding_clear_button))
                    .transparentTarget(true)
                    .id(1)
                    .cancelable(false),
                TapTarget
                    .forView(binding.onboardingOkButton, activity.getString(R.string.onboarding_thats_it))
                    .transparentTarget(true)
                    .id(2)
                    .cancelable(false)
            )
            .listener(object : TapTargetSequence.Listener {
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                    if (lastTarget?.id() == 1) {
                        binding.onboardingOkButton.visibility = View.VISIBLE
                    }
                }

                override fun onSequenceFinish() {
                    binding.onboardingOkButton.visibility = View.GONE
                    onboardingFinished(activity)
                }

                override fun onSequenceCanceled(lastTarget: TapTarget?) {}

            })
            .start()
    }

    private fun onboardingFinished(activity: Activity) {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        sharedPreferences
            .edit()
            .putBoolean(MainFragment.ONBOARDING_FINISHED_SP_KEY, true)
            .apply()
    }
}