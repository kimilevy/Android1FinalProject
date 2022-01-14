package com.hit.android1.finalproject.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BoundActivity<T : ViewBinding?> : AppCompatActivity() {
    protected var binding: T? = null
    protected var view: View? = null
    protected open fun runOnCreate(view: View) {}

    // Abstract Functions
    abstract fun inflate(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate()
        view = binding!!.root
        runOnCreate(view!!)
        setContentView(view)
    }

}