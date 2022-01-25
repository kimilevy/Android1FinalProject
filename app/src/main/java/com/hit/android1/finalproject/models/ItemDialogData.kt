package com.hit.android1.finalproject.models

import com.hit.android1.finalproject.dao.relations.InventoryItemWithRecipes
import kotlinx.serialization.Serializable

@Serializable
data class ItemDialogData (val inventoryItemWithRecipes: InventoryItemWithRecipes)

