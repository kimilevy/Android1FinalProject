package com.hit.android1.finalproject.app

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.dao.AppDatabase

object Globals {
    lateinit var db: AppDatabase
    var sfxPlayer: SFXPlayer? = null
    val dao get() = db.dao()

    fun createDB(applicationContext: Context): AppDatabase {
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "game-database")
        .createFromAsset("database/game-database.db")
        .build()
        return db
    }

    fun createSFX(context: Context) {
        sfxPlayer = SFXPlayer(context)
        sfxPlayer?.startBGMusic(context)
    }
}