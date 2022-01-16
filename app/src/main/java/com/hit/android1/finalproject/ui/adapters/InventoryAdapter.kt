package com.hit.android1.finalproject.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.ui.customviews.ItemView

class InventoryAdapter(var inventory: List<InventoryItem>) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    class InventoryViewHolder(var view: ItemView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = ItemView(parent.context)
        view.isDragAndDrop = true
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.view.item = inventory[position]
    }

    override fun getItemCount(): Int = inventory.size


}