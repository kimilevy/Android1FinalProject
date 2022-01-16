package com.hit.android1.finalproject.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.dao.entities.Recipe
import com.hit.android1.finalproject.dao.relations.InventoryItemWithRecipes
import com.hit.android1.finalproject.dao.relations.RecipeAndIngredients


@Dao
interface IDao {
    /***********     Recipe      ************/

    @Transaction
    @Query("SELECT * FROM recipe WHERE (:itemId IS NULL OR result=:itemId)")
    suspend fun getRecipesWithIngredients(itemId: String? = null): List<RecipeAndIngredients>

    @Query("SELECT * FROM recipe WHERE (:itemId IS NULL OR result=:itemId)")
    suspend fun getRecipes(itemId: String? = null): List<Recipe>

    @Transaction
    @Query("SELECT result FROM recipe WHERE (first_ingredient=:itemA AND second_ingredient=:itemB) OR (first_ingredient=:itemB AND second_ingredient=:itemA)")
    suspend fun getResult(itemA: String, itemB: String): String

    /***********     Item      ************/

    @Query("SELECT id FROM inventoryItem")
    suspend fun getItemNames(): List<String>

    @Query("SELECT * FROM inventoryItem")
    suspend fun getItems(): List<InventoryItem>

    @Query("SELECT * FROM inventoryItem WHERE unlocked = 1")
    suspend fun getUnlockedItems(): List<InventoryItem>

    @Query("SELECT unlocked FROM inventoryItem WHERE id IN (:itemIds)")
    suspend fun isUnlocked(itemIds: List<String>): List<Boolean>

    @Query("SELECT unlocked FROM inventoryItem WHERE id IN (:itemId)")
    suspend fun isUnlocked(itemId: String): Boolean

    @Query("SELECT * FROM inventoryItem WHERE id=:itemId")
    suspend fun getItem(itemId: String): InventoryItem

    @Query("UPDATE inventoryItem SET unlocked=1 WHERE id=:itemId")
    suspend fun unlockItem(itemId: String): Int

    @Transaction
    @Query("SELECT * FROM inventoryItem WHERE id=:itemId")
    suspend fun getItemWithRecipes(itemId: String): InventoryItemWithRecipes
}