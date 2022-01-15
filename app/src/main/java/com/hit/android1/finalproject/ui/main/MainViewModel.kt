package com.hit.android1.finalproject.ui.main

import androidx.lifecycle.ViewModel
import com.hit.android1.finalproject.dao.entities.InventoryItem
import kotlin.random.Random

class MainViewModel : ViewModel() {
    var items: List<InventoryItem>? = null
    fun getRandomItem() = items?.let {it[Random.nextInt(0, it.size - 1)]}

    // TODO: Implement the ViewModel
    var cool = "COOL TEST!🎇"


}