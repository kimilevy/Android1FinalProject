package com.hit.android1.finalproject.app

import android.content.Context
import androidx.room.Room
import com.hit.android1.finalproject.dao.AppDatabase

object Globals {
    lateinit var db: AppDatabase
    val dao get() = db.dao()

    fun createDB(applicationContext: Context): AppDatabase {
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "game-database")
        .createFromAsset("database/game-database.db")
        .build()
        return db
    }
}