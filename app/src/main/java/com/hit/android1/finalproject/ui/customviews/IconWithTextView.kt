package com.hit.android1.finalproject.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.CustomView
import com.hit.android1.finalproject.databinding.CustomViewIconWithTextBinding

class IconWithTextView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CustomView<CustomViewIconWithTextBinding>(context, attrs, defStyle) {
    override fun inflate() = CustomViewIconWithTextBinding.inflate(LayoutInflater.from(context), this, true)

    override fun initView(context: Context, attrs: AttributeSet?, defStyle: Int?) {
        setAttributes(attrs, context)

    }

    open fun setOnClickListener(callback: () -> Unit) = binding.imageButton.setOnClickListener { callback() }

    private fun setAttributes(attrs: AttributeSet?, context: Context) {
        attrs?.let {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.IconWithTextView,
                0,
                0
            ).apply {
                try {
                    val src = getDrawable(R.styleable.IconWithTextView_iconSrc)
                    binding.imageButton.setImageDrawable(src)

                    val text = getString(R.styleable.IconWithTextView_text)
                    if (text != null) {
                        binding.textView.text = text
                    } else {
                        binding.layout.removeView(binding.textView)
                        binding.layout.invalidate()
                    }
                }
                finally {
                    recycle()
                }
            }
        }
    }
}