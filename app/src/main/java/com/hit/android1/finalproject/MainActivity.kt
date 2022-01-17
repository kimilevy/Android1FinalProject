package com.hit.android1.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import com.hit.android1.finalproject.app.Extensions.logDebug
import com.hit.android1.finalproject.app.Extensions.openSnackbar
import com.hit.android1.finalproject.app.Globals
import com.hit.android1.finalproject.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Globals.createDB(applicationContext)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}