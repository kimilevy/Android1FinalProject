package com.hit.android1.finalproject.ui.customviews

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.Extensions.logDebug
import com.hit.android1.finalproject.app.Extensions.toPx
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.dao.entities.InventoryItem.Companion.name
import com.hit.android1.finalproject.dao.relations.RecipeAndIngredients
import com.hit.android1.finalproject.databinding.PopupViewBinding
import com.hit.android1.finalproject.models.ItemDialogData
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.util.*
import kotlin.concurrent.fixedRateTimer

class ItemDialogFragment: DialogFragment() {
    companion object {
        const val TAG = "ItemDialogFragment"
    }

    private var animationTimer: Timer? = null
    protected var _binding: PopupViewBinding? = null
    protected val binding get() = _binding!!
//
//    override fun getTheme(): Int {
//        return R.style.ItemDialogFragment
//    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PopupViewBinding.inflate(LayoutInflater.from(context), container, false)
        requireDialog().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val bundle: Bundle? = arguments
        bundle?.let {
            it.getString("DATA")?.let { json ->
                logDebug(json)
                val data = Json.decodeFromString<ItemDialogData>(json)
                data.let {
                    // Here we (probably) have all the data the dialog needs to work
                    binding.itemName.text = data.inventoryItemWithRecipes.item.name(requireContext())
                    GlobalScope.launch(Dispatchers.IO) {
                        val recipes = dao.getRecipesWithIngredients(data.inventoryItemWithRecipes.item.id)
                        logDebug(recipes)
                        if (recipes.isNotEmpty()) {
                            val recipesLength = recipes.size
                            startRecipeLoop(recipesLength, recipes)
                        } else {
                            binding.recipesLayout.visibility = View.GONE
                            binding.baseMaterialText.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        return binding.root
    }

    class AnimationLooper

    private fun startRecipeLoop(
        recipesLength: Int,
        recipes: List<RecipeAndIngredients>
    ) {
        var i = 0
        logDebug("Starting animation")
        animationTimer = fixedRateTimer("items_animation", false, 0L, 1000) {
            this@ItemDialogFragment.activity?.runOnUiThread {
                binding.firstIngredient.item = recipes[i].first_ingredient
                binding.secondIngredient.item = recipes[i].second_ingredient
            }
            i += 1
            i %= recipesLength
        }


        logDebug("animationjob is running?" + animationTimer)
    }

    override fun onDestroy() {
        animationTimer?.cancel()
        super.onDestroy()
    }
}