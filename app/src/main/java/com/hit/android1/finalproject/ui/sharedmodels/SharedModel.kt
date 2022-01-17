package com.hit.android1.finalproject.ui.sharedmodels

import androidx.lifecycle.ViewModel
import com.hit.android1.finalproject.app.Globals.dao
import com.hit.android1.finalproject.dao.entities.InventoryItem
import kotlin.random.Random

class SharedModel : ViewModel() {
    var items: List<InventoryItem>? = null
    var unlockedItems: MutableList<InventoryItem>? = null
    private var unlockedListeners = mutableListOf<suspend (item: InventoryItem) -> Unit>()
    fun getRandomItem() = items?.let {it[Random.nextInt(0, it.size - 1)]}
    suspend fun unlockItem(item: InventoryItem) {
        item.unlocked = true
        items?.find {
            it.id == item.id
        }?.unlocked = true
        unlockedItems?.add(item)
        dao.unlockItem(item.id)
        unlockedListeners.forEach { it(item) }
    }
    fun addUnlockedListener(callback: suspend (item: InventoryItem) -> Unit) {
        unlockedListeners.add(callback)
    }
}