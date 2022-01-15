package com.hit.android1.finalproject.dao.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.dao.entities.Recipe

data class RecipeAndIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "first_ingredient",
        entityColumn = "id"
    )
    val first_ingredient: InventoryItem,
    @Relation(
        parentColumn = "second_ingredient",
        entityColumn = "id"
    )
    val second_ingredient: InventoryItem,
)
