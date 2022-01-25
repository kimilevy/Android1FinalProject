package com.hit.android1.finalproject.app

import android.util.Log
import androidx.fragment.app.Fragment
import java.text.Normalizer


object Extensions {
    fun Fragment.logDebug(v: Any?) = Log.d("Debug", v.toString())

    fun String.stripHebrewVowels(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFKD)
            .replace(Regex("[\u0591-\u05C7]"),"")
    }

    fun <K, V> MutableMap<K, V>.getOrCreate(key: K, create: () -> V): V {
        if (!this.containsKey(key))
            this[key]=create.invoke()
        return this[key]!!
    }
}