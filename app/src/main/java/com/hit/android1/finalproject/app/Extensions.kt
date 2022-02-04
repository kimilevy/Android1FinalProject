package com.hit.android1.finalproject.app

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.hit.android1.finalproject.BuildConfig
import com.hit.android1.finalproject.R
import com.hit.android1.finalproject.app.Extensions.openSnackbar
import java.text.Normalizer


object Extensions {
    fun Fragment.logDebug(v: Any?) {
        if (BuildConfig.DEBUG) {
            val text = v.toString()
            Log.d("Debug", text)
        }
    }
    fun View.logDebug(v: Any?) {
        if (BuildConfig.DEBUG) {
            val text = v.toString()
            Log.d("Debug", text)
            Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun String.stripHebrewVowels(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFKD)
            .replace(Regex("[\u0591-\u05C7]"),"")
    }

    fun <K, V> MutableMap<K, V>.getOrCreate(key: K, create: () -> V): V {
        if (!this.containsKey(key))
            this[key]=create.invoke()
        return this[key]!!
    }

    fun View.openSnackbar(text: String) = Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
}