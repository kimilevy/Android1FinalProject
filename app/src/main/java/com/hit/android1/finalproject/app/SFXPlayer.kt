package com.hit.android1.finalproject.app

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.IdRes
import androidx.annotation.RawRes
import com.hit.android1.finalproject.R

class SFXPlayer(context: Context) {
    private var sfx: MutableMap<Int, MediaPlayer>? = null

    init {
        sfx = mutableMapOf()
        sfx?.let {
            it[R.raw.created_item] = MediaPlayer.create(context, R.raw.created_item)
            it[R.raw.pop_compressed] = MediaPlayer.create(context, R.raw.pop_compressed)
            it[R.raw.clean] = MediaPlayer.create(context, R.raw.clean)
            it[R.raw.lift_sound_pitch_enhanced] = MediaPlayer.create(context, R.raw.lift_sound_pitch_enhanced)
        }
    }

    fun play(@RawRes resourceId: Int) {
        sfx?.get(resourceId)?.seekTo(0)
        sfx?.get(resourceId)?.start()
    }

    fun startBGMusic(context: Context) {
        sfx?.let {
         it[R.raw.bg_music_compressed] = MediaPlayer.create(context, R.raw.bg_music_compressed).let {
             it.isLooping = true
             it.setVolume(.3f, .3f)
             it.start()
             it
         }
        }
    }

    fun pauseBGMusic() {
        sfx?.let {
            it[R.raw.bg_music_compressed]?.pause()
        }
    }

    fun resumeBGMusic() {
        sfx?.let {
            it[R.raw.bg_music_compressed]?.start()
        }
    }
}