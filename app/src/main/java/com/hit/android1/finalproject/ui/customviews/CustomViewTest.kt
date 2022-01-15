package com.hit.android1.finalproject.ui.customviews

import android.content.Context
import android.util.AttributeSet

import android.widget.FrameLayout
import com.hit.android1.finalproject.R
import java.io.IOException
import java.io.InputStream


class CustomViewTest : FrameLayout {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initView()
    }

    constructor(context: Context?) : super(context!!) {
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.custom_view, this)
    }
}