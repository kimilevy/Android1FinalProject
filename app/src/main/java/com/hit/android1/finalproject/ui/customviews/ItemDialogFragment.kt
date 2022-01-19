package com.hit.android1.finalproject.ui.customviews

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.KeyFrames
import androidx.fragment.app.DialogFragment
import com.hit.android1.finalproject.dao.entities.InventoryItem.Companion.name
import com.hit.android1.finalproject.dao.relations.RecipeAndIngredients
import com.hit.android1.finalproject.databinding.CustomViewItemBinding
import com.hit.android1.finalproject.databinding.PopupViewBinding
import com.hit.android1.finalproject.models.ItemDialogData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ItemDialogFragment: DialogFragment() {
    protected var _binding: PopupViewBinding? = null
    protected val binding get() = _binding!!
    var recipes: List<RecipeAndIngredients>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= PopupViewBinding.inflate(LayoutInflater.from(context), container, false)
        val bundle: Bundle? = arguments
        bundle?.let {
            it.getString("JSON")?.let { json ->
                val data = Json.decodeFromString<ItemDialogData>(json)
                binding.itemName.text=data.result.name()
                recipes = data.recipes
                binding.firstIngredient.item = recipes!![0].first_ingredient
                binding.secondIngredient.item = recipes!![0].second_ingredient
//                val recipesLength = recipes!!.size
//                val keyframes = mutableListOf<Keyframe>()
//                recipes!!.forEachIndexed { index, recipeAndIngredients ->
//                    keyframes.add(Keyframe.ofObject(index.toFloat()/recipesLength, recipeAndIngredients.first_ingredient))
//                }
//                animateItem(keyframes, binding.firstIngredient, recipesLength)
            }
        }
        return binding.root
    }
//
//    private fun animateItem(
//        keyframes: MutableList<Keyframe>,
//        ingredient: ItemView,
//        recipesLength: Int
//    ) {
//        val pvh = PropertyValuesHolder.ofKeyframe("item", *keyframes.toTypedArray())
//        val animation = ObjectAnimator.ofPropertyValuesHolder(ingredient, pvh)
//        animation.duration = recipesLength * 1000L
//        animation.repeatMode = ObjectAnimator.RESTART
//        animation.repeatCount = ObjectAnimator.INFINITE
//        animation.start()
//    }
}