package com.hit.android1.finalproject

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import com.hit.android1.finalproject.app.Globals
import com.hit.android1.finalproject.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    companion object {
        var PACKAGE_NAME: String? = null
    }
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Globals.createDB(applicationContext)
        Globals.createSFX(applicationContext)
        PACKAGE_NAME = applicationContext.packageName;

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = MainActivityBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)
    }

    override fun onStop() {
        super.onStop()
        Globals.sfxPlayer?.pauseBGMusic()
    }

    override fun onResume() {
        super.onResume()
        Globals.sfxPlayer?.resumeBGMusic()
    }
}