package com.hit.android1.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.hit.android1.finalproject.app.Globals
import com.hit.android1.finalproject.app.Extensions.logDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Globals.createDB(applicationContext)
        setContentView(R.layout.main_activity)
    }
}