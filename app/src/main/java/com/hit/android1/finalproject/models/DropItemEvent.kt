package com.hit.android1.finalproject.models

import com.hit.android1.finalproject.dao.entities.InventoryItem
import kotlinx.serialization.Serializable

@Serializable
data class DropItemEventData(val item: InventoryItem, val draggable: Boolean)