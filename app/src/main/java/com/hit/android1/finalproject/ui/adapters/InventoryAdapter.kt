package com.hit.android1.finalproject.ui.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.ui.customviews.ItemDialogFragment
import com.hit.android1.finalproject.ui.customviews.ItemView

class InventoryAdapter(val isDroppable: Boolean = true, var onItemClick: ((InventoryItem?) -> Unit)? = null) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {
    inner class InventoryViewHolder(var view: ItemView) : RecyclerView.ViewHolder(view)

    var inventory: MutableList<InventoryItem> = mutableListOf()
        set(value) {
            field=value
            notifyItemRangeInserted(0, value.size)
        }

    fun unlockItem(item: InventoryItem, inserted: Boolean = true) {
        if (inventory.contains(item)) return
        inventory.add(item)
        inventory.sortBy { it.id }

        if (inserted) {
            notifyItemInserted(inventory.indexOf(item))
        } else {
            notifyItemChanged(inventory.indexOf(item))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = ItemView(parent.context)
        view.isDragAndDrop = isDroppable
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.view.item = inventory[position]
        holder.view.setOnClickListener {
            val itemView = it as ItemView
            Log.d("onBindViewHolder", "onBindViewHolder: Test clicking something")
            onItemClick?.let { it1 -> it1(itemView.item) }
        }
    }

    override fun getItemCount(): Int = inventory.size
}