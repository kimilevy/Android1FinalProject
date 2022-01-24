package com.hit.android1.finalproject.ui.adapters

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.hit.android1.finalproject.app.Extensions.stripHebrewVowels
import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.dao.entities.InventoryItem.Companion.name
import com.hit.android1.finalproject.ui.customviews.ItemView

class InventoryAdapter(val context: Context, val isDroppable: Boolean = true, var onItemClick: ((InventoryItem?) -> Unit)? = null) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>(), Filterable {
    inner class InventoryViewHolder(var view: ItemView) : RecyclerView.ViewHolder(view)

    var inventory: MutableList<InventoryItem> = mutableListOf()
        set(value) {
            field=value
            fullList = mutableListOf<InventoryItem>().apply{ addAll(value) }
            notifyItemRangeInserted(0, value.size)
        }

    var fullList: MutableList<InventoryItem> = mutableListOf()

    private val searchFilter by lazy {
        object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = arrayListOf<InventoryItem>()

                Log.d("Text now is: ", constraint.isNullOrEmpty().toString())
                val list = if (constraint.isNullOrEmpty()) {
                    fullList
                } else {
                    val filterPattern = constraint.toString().trim()
                    fullList.filter { i -> i.name(context).stripHebrewVowels().contains(filterPattern, ignoreCase = true) }
                }
                filteredList += list

                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                inventory.clear()
                results?.let {
                    inventory.addAll(it.values as Collection<InventoryItem>)
                    notifyDataSetChanged()
                }
            }

        }
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

    override fun getFilter(): Filter = searchFilter


}