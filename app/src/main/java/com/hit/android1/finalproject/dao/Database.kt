package com.hit.android1.finalproject.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hit.android1.finalproject.dao.entities.InventoryItem
import com.hit.android1.finalproject.dao.entities.Recipe

@Database(entities = [Recipe::class, InventoryItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): IDao
}