package com.hit.android1.finalproject.dao.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name="result")
    val result: String,

    @ColumnInfo(name="first_ingredient")
    val first_ingredient: String,

    @ColumnInfo(name="second_ingredient")
    val second_ingredient: String,
) {
    override fun toString(): String {
        return "Recipe($id, $result): $first_ingredient + $second_ingredient"
    }
}
