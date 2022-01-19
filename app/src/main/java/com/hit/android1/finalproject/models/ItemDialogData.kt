package com.hit.android1.finalproject.models

import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.dao.entities.Recipe
import com.hit.android1.finalproject.dao.relations.InventoryItemWithRecipes
import com.hit.android1.finalproject.dao.relations.RecipeAndIngredients
import kotlinx.serialization.Serializable

@Serializable
data class ItemDialogData (val inventoryItemWithRecipes: InventoryItemWithRecipes)

