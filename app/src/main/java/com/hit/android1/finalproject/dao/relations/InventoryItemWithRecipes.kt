package com.hit.android1.finalproject.dao.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.dao.entities.Recipe
import kotlinx.serialization.Serializable

@Serializable
data class InventoryItemWithRecipes(
    @Embedded val item: InventoryItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "result"
    )
    val recipes: List<Recipe>
)
