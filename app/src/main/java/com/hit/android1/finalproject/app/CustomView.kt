package com.hit.android1.finalproject.app

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding

abstract class CustomView<T: ViewBinding>  @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context!!, attrs, defStyle) {
    private lateinit var _view: View
    private val _binding: T? by lazy { inflate() }
    protected val binding get() = _binding!!

    // Abstract Functions
    abstract fun inflate(): T

    // Initialize view
    protected open fun initView(context: Context, attrs: AttributeSet? = null, defStyle: Int? = null) {}

    init {
        initView(context!!, attrs)
    }
}