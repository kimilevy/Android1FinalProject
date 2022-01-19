package com.hit.android1.finalproject.models

import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.dao.relations.InventoryItemWithRecipes
import com.hit.android1.finalproject.dao.relations.RecipeAndIngredients

data class ItemDialogData ( val result:InventoryItem, val recipes: List<RecipeAndIngredients>)

