package com.hit.android1.finalproject.ui.customviews

import android.content.ClipDescription
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hit.android1.finalproject.app.CustomView
import com.hit.android1.finalproject.app.Extensions.logDebug
import com.hit.android1.finalproject.databinding.CustomViewAlchemyPlaygroundBinding
import com.hit.android1.finalproject.models.DropItemEventData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.view.MotionEvent
import com.hit.android1.finalproject.app.Extensions.openSnackbar
import kotlin.math.abs


class AlchemyPlaygroundView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CustomView<CustomViewAlchemyPlaygroundBinding>(context, attrs, defStyle) {
    override fun inflate() = CustomViewAlchemyPlaygroundBinding.inflate(LayoutInflater.from(context), this, true)
    var items = mutableListOf<ItemView>()
    var movedItem: ItemView? = null
    var itemViewHeight: Int? = null
    var itemViewWidth: Int? = null
    val dropXOffset = itemViewWidth?.let {-1 * it * .8f}
    val dropYOffset = itemViewHeight?.let {-1 * it * 1.6f}
    val xOffset get() = itemViewWidth?.let {-1 * it * .8f}
    val yOffset get() = itemViewHeight?.let {-1 * it * 1.6f}
    override fun initView(context: Context, attrs: AttributeSet?, defStyle: Int?) {
        registerDropListener()
    }


    private fun registerDropListener() {
        binding.root.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DROP -> {
                    dragEvent.clipDescription?.let {
                        if (it.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            val dropDataJson = dragEvent.clipData.getItemAt(0).text.toString()
                            logDebug(dropDataJson)
                            GlobalScope.launch {
                                val dropData = Json.decodeFromString<DropItemEventData>(dropDataJson)
                                val newItem = ItemView(context, dropData.item, false)
                                setItemTouchListener(newItem)
                                items.add(newItem)
                                mergeIfIntersecting(newItem)
                                withContext(Dispatchers.Main) {
                                    newItem.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                    itemViewHeight = itemViewHeight ?: newItem.measuredHeight
                                    itemViewWidth = itemViewWidth ?: newItem.measuredWidth
                                    newItem.x = dragEvent.x + (xOffset?:0f)
                                    newItem.y = dragEvent.y + (yOffset?:0f)
                                    binding.root.addView(newItem)
                                    invalidate()
                                }
                            }
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setItemTouchListener(item: ItemView) {
        item.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    movedItem = item
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    movedItem?.let {
                        it.x = event.rawX + (xOffset?:0f)
                        it.y = event.rawY + (yOffset?:0f)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    movedItem?.let {
                        mergeIfIntersecting(it)
                    }

                    movedItem = null
                    view.performClick();
                    true
                }
                else -> false
            }
        }
    }

    private fun mergeIfIntersecting(item: ItemView) {
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
        val intersecting = items.filter { curr ->

            val xDiff = abs(item.x - curr.x)
            val yDiff = abs(item.y - curr.y)
            curr != item && xDiff < (itemViewWidth!! * 1.1) && yDiff < (itemViewHeight!! * 1.1)
        }
        if (intersecting.size == 0) return;
        val otherItemIntersectingWith = intersecting[0]



    }
}