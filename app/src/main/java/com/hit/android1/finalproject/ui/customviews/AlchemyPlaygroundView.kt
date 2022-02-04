package com.hit.android1.finalproject.ui.customviews

import android.animation.*
import android.content.ClipDescription
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hit.android1.finalproject.app.CustomView
import com.hit.android1.finalproject.databinding.CustomViewAlchemyPlaygroundBinding
import com.hit.android1.finalproject.models.DropItemEventData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.view.MotionEvent
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.Extensions.logDebug
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.app.Globals.sfxPlayer
import com.hit.android1.finalproject.dao.entities.InventoryItem
import kotlinx.coroutines.*
import kotlin.math.abs


class AlchemyPlaygroundView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CustomView<CustomViewAlchemyPlaygroundBinding>(context, attrs, defStyle) {

    companion object {
        private const val MERGE_ANIMATION_DURATION = 300L
        private const val ITEM_WIDTH_X_OFFSET = .8f
        private const val ITEM_WIDTH_Y_OFFSET = 1.9f
        private const val MAX_GRAVITY_X_DIFF = 1.2 // in regards to width
        private const val MAX_GRAVITY_Y_DIFF = 1 // in regards to height
    }

    override fun inflate() = CustomViewAlchemyPlaygroundBinding.inflate(LayoutInflater.from(context), this, true)
    private var items = mutableListOf<ItemView>()
    private var movedItem: ItemView? = null
    private var itemViewHeight: Int? = null
    private var itemViewWidth: Int? = null
    private var onDropListeners = mutableListOf<(ItemView) -> Unit>()
    private var onceOnDropListeners = mutableListOf<(ItemView) -> Unit>()
    private val xOffset get() = itemViewWidth?.let {-1 * it * ITEM_WIDTH_X_OFFSET}
    private val yOffset get() = itemViewHeight?.let {-1 * it * ITEM_WIDTH_Y_OFFSET}
    override fun initView(context: Context, attrs: AttributeSet?, defStyle: Int?) {
        registerDropListener()
    }

    private var onItemMerge: (item: InventoryItem) -> Unit = {}

    fun setOnItemMerge(callback: (item: InventoryItem) -> Unit) {
        onItemMerge = callback
    }

    fun addOnDropListener(callback: (item: ItemView) -> Unit) {
        onDropListeners += callback
    }

    fun addOnceOnDropListener(callback: (item: ItemView) -> Unit) {
        onceOnDropListeners += callback
    }

    fun clear() {
        binding.alchemyPlaygroundLayout.removeAllViews()
        items.clear()
        movedItem = null
    }

    private fun registerDropListener() {
        binding.root.setOnDragListener { _, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DROP -> {
                    val x = dragEvent.x
                    val y = dragEvent.y
                    logDebug("Dropped item")
                    dragEvent.clipDescription?.let {
                        val clipData = dragEvent.clipData
                        logDebug("Item has clip description")
                        if (it.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            val dropDataJson = clipData.getItemAt(0).text.toString()
                            logDebug("Json was $dropDataJson")
                            GlobalScope.launch {
                                val dropData = Json.decodeFromString<DropItemEventData>(dropDataJson)
                                logDebug("Parsed data is $dropData")
                                sfxPlayer?.play(R.raw.pop_compressed)
                                createNewItem(
                                    dropData.item,
                                    x,
                                    y,
                                    shouldMergeIfIntersecting = true,
                                    useXOffset = true,
                                    useYOffset = false,
                                    isDrop = true
                                )

                            }
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private suspend fun createNewItem(
        itemToCreate: InventoryItem,
        creationX: Float,
        creationY: Float,
        shouldMergeIfIntersecting: Boolean,
        useXOffset: Boolean,
        useYOffset: Boolean,
        isDrop: Boolean
    ): ItemView {
        val newItem = ItemView(context, itemToCreate, false)
        setItemTouchListener(newItem)
        items.add(newItem)
        withContext(Dispatchers.Main) {
            newItem.measure(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            itemViewHeight = itemViewHeight ?: newItem.measuredHeight
            itemViewWidth = itemViewWidth ?: newItem.measuredWidth
            logDebug("useOffset, $useXOffset, creation: ($creationX, $creationY), offset: ($xOffset, $yOffset)")
            newItem.x = creationX + (if(useXOffset) xOffset!! else 0f)
            newItem.y = creationY + (if(useYOffset) yOffset!! else 0f)
            binding.root.addView(newItem)
            if (shouldMergeIfIntersecting) {
                mergeIfIntersecting(newItem, isDrop)
            }

            runOnDropListeners(newItem)
            runOnceOnDropListeners(newItem)
        }
        return newItem
    }

    private fun runOnDropListeners(newItem: ItemView) {
        onDropListeners.forEach {
            it(newItem)
        }
    }

    private fun runOnceOnDropListeners(newItem: ItemView) {
        onceOnDropListeners.forEach {
            it(newItem)
        }
        onceOnDropListeners.clear()
    }

    private fun setItemTouchListener(item: ItemView) {
        item.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    movedItem = item
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    movedItem?.let {
                        it.x = (event.rawX + (xOffset?:0f)).coerceIn(0f, width.toFloat() - ((itemViewWidth)!! * 1.2f))
                        it.y = (event.rawY + (yOffset?:0f)).coerceIn(0f, height.toFloat() - itemViewHeight!!)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    movedItem?.let {
                        mergeIfIntersecting(it, false)
                    }

                    movedItem = null
                    performClick()
                    true
                }
                else -> false
            }
        }
    }

    @DelicateCoroutinesApi
    private fun mergeIfIntersecting(item: ItemView, isDrop: Boolean) {
        /*
            check if matching with intersected item views
            get the result of them
            animate them to gravitate to the middle of them
            when intersection threshold reaches:
                create result item (don't add to view)
                remove both items from items array and layout
                add resulting item
                set resulting item as unlocked (with dao)
                use listener from parent to tell them the item is unlocked
         */
        GlobalScope.launch(Dispatchers.IO) {
            logDebug("Checking if intersection")
            val intersecting = items.filter { curr ->
                val xDiff = abs(item.x - curr.x)
                val yDiff = abs(item.y - curr.y)
                curr != item &&
                        xDiff < (itemViewWidth!! * MAX_GRAVITY_X_DIFF) &&
                        yDiff < (itemViewHeight!! * MAX_GRAVITY_Y_DIFF)
            }

            if (intersecting.isEmpty()) {
                logDebug("No intersection, finishing check")
                return@launch
            }
            val recipes = dao.getRecipesThatCanBeMade(item.item!!.id)
            val viableIntersections = intersecting.filter { curr ->
                recipes.any{it.second_ingredient == curr.item?.id || it.first_ingredient == curr.item?.id}
            }

            if (viableIntersections.isEmpty()) {
                logDebug("No viable recipes, returning.")
                return@launch
            }
            val otherItemIntersectingWith = viableIntersections[0]
            val resultId = dao.getResult(otherItemIntersectingWith.item!!.id, item.item!!.id)
            resultId?.let {
                mergeItems(item, otherItemIntersectingWith, dao.getItem(resultId), isDrop)
                sfxPlayer?.play(R.raw.created_item)
            }
        }
    }

    private suspend fun mergeItems(itemA: ItemView, itemB: ItemView, result: InventoryItem, isDrop: Boolean) {
        logDebug("Merging items")
        // Tell parent view that we merged something
        onItemMerge(result)

        // Disabled touch on both items.
        itemA.setOnTouchListener(null)
        itemB.setOnTouchListener(null)
        withContext(Dispatchers.Main) {

            // Animate both items and wait for them to finish
            mergeItemsAnimation(itemA, itemB) { midX, midY, _ ->
                // Remove both items
                binding.root.removeView(itemA)
                binding.root.removeView(itemB)

                // Create new result item
                GlobalScope.launch {
                    items.remove(itemA)
                    items.remove(itemB)
                    val resultItem = createNewItem(
                        result,
                        midX,
                        midY,
                        shouldMergeIfIntersecting = false,
                        useXOffset = false,
                        useYOffset = false,
                        isDrop = false
                    )

                    withContext(Dispatchers.Main) {
                        resultItem.scaleX = 0f
                        resultItem.scaleY = 0f

                        // Animate result item
                        resultItemAnimation(resultItem)
                    }
                }
            }
        }
    }

    private fun mergeItemsAnimation(itemA: ItemView, itemB: ItemView, onAnimationEnd: (Float, Float, Animator) -> Unit) {
        val middleX = (itemA.x + itemB.x) / 2
        val middleY = (itemA.y + itemB.y) / 2

        val animations = listOf(
            ObjectAnimator.ofFloat(itemA, "translationX", middleX),
            ObjectAnimator.ofFloat(itemA, "translationY", middleY),
            ObjectAnimator.ofFloat(itemB, "translationX", middleX),
            ObjectAnimator.ofFloat(itemB, "translationY", middleY)
        )
        animations.forEach {
            it.duration = MERGE_ANIMATION_DURATION
        }
        val makeAXSmall = ObjectAnimator.ofFloat(itemA, "scaleX", 0f)
        val makeAYSmall = ObjectAnimator.ofFloat(itemA, "scaleY", 0f)
        val makeBXSmall = ObjectAnimator.ofFloat(itemB, "scaleX", 0f)
        val makeBYSmall = ObjectAnimator.ofFloat(itemB, "scaleY", 0f)

        AnimatorSet().apply {
            playTogether(animations)
            play(makeAXSmall).after(animations.last())
            play(makeAYSmall).after(animations.last())
            play(makeBXSmall).after(animations.last())
            play(makeBYSmall).after(animations.last())
            start()
        }.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                onAnimationEnd(middleX, middleY, animator)
            }
        })
    }

    private fun resultItemAnimation(result: ItemView) {
        val firstX = ObjectAnimator.ofFloat(result, "scaleX", 1.7f)
        val firstY = ObjectAnimator.ofFloat(result, "scaleY", 1.7f)
        firstX.duration = 300
        firstY.duration = 300

        val secondX = ObjectAnimator.ofFloat(result, "scaleX", 1f)
        val secondY = ObjectAnimator.ofFloat(result, "scaleY", 1f)
        secondX.duration = 150
        secondY.duration = 150

        AnimatorSet().apply {
            play(firstX).with(firstY)
            play(secondX).with(secondY).after(firstX)
            start()
        }
    }
}