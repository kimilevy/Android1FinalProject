package com.hit.android1.finalproject.ui.customviews

import android.content.ClipDescription
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import com.hit.android1.finalproject.app.CustomView
import com.hit.android1.finalproject.app.Extensions.logDebug
import com.hit.android1.finalproject.app.Extensions.openSnackbar
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.databinding.CustomViewAlchemyPlaygroundBinding
import com.hit.android1.finalproject.databinding.CustomViewItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlchemyPlaygroundView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CustomView<CustomViewAlchemyPlaygroundBinding>(context, attrs, defStyle) {
    override fun inflate() = CustomViewAlchemyPlaygroundBinding.inflate(LayoutInflater.from(context), this, true)
    var items = mutableListOf<ItemView>()
    override fun initView(context: Context, attrs: AttributeSet?, defStyle: Int?) {
        registerDropListener()
    }

    private fun registerDropListener() {
        binding.root.setOnDragListener { view, dragEvent ->
            //2
            val draggableItem = dragEvent.localState as View

            //3
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    true
                }
                DragEvent.ACTION_DROP -> {
                    dragEvent.clipDescription?.let {
                        if (it.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            val itemId = dragEvent.clipData.getItemAt(0).text.toString()
                            GlobalScope.launch(Dispatchers.IO) {
                                val itemDropped = dao.getItem(itemId)
                                val newItem = ItemView(context, itemDropped)
                                items.add(newItem)
                                withContext(Dispatchers.Main) {
                                    newItem.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                    newItem.x = ((dragEvent.x - newItem.measuredWidth * .8).toFloat())
                                    newItem.y = (dragEvent.y - newItem.measuredHeight * 1.6).toFloat()
                                    binding.root.addView(newItem)
                                    invalidate()
                                }
                            }
                        }
                    }
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}