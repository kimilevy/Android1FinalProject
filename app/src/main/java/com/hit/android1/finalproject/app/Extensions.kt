package com.hit.android1.finalproject.app

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.icu.text.Normalizer2
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern


object Extensions {

    fun Activity.logDebug(v: Any?) = Log.d("Debug", v.toString())
    fun Activity.logException(e: Exception) = Log.e("Error", e.toString())
    fun Activity.logException(v: String) = Log.e("Error", v.toString())

    fun View.logDebug(v: Any?) = Log.d("Debug", v.toString())
    fun View.logException(e: Exception) = Log.e("Error", e.toString())
    fun View.logException(v: String) = Log.e("Error", v.toString())

    fun Fragment.logDebug(v: Any?) = Log.d("Debug", v.toString())
    fun Fragment.logException(e: Exception) = Log.e("Error", e.toString())
    fun Fragment.logException(e: String) = Log.e("Error", e)

    fun View.isRtl() = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

    fun String.capitalize() = this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
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

    fun AppCompatEditText.toFloat() = this.text.toString().toFloat()
    fun AppCompatEditText.toFloatOrNull() = this.text.toString().toFloatOrNull()

    fun AppCompatEditText.toInt() = this.text.toString().toInt()
    fun AppCompatEditText.toIntOrNull() = this.text.toString().toIntOrNull()

    fun Editable?.toInt() = this.toString().toInt()
    fun Editable?.toIntOrNull() = this.toString().toIntOrNull()
    fun Editable?.toFloat() = this.toString().toFloat()
    fun Editable?.toFloatOrNull() = this.toString().toFloatOrNull()

    fun Fragment.getInt(resourceId: Int) = this.getString(resourceId).toInt()
    fun Fragment.getFloat(resourceId: Int) = this.getString(resourceId).toFloat()
    fun Fragment.openErrorSnackbar(errorText: Int) { // ADD TRANSLATION FOR ERROR
//        val snackbar = Snackbar.make(requireView(), errorText, Snackbar.LENGTH_SHORT)
//            .setTextColor(ContextCompat.getColor(requireContext(), R.color.error_snackbar_txt))
//        snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error_snackbar))
//        snackbar.show()
    }
    fun Fragment.openErrorSnackbar(errorText: String) { // ADD TRANSLATION FOR ERROR
//        val snackbar = Snackbar.make(requireView(), errorText, Snackbar.LENGTH_SHORT)
//            .setTextColor(ContextCompat.getColor(requireContext(), R.color.error_snackbar_txt))
//        snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error_snackbar))
//        snackbar.show()
    }
    fun View.openSnackbar(text: Any) {
        Snackbar.make(this, text.toString(), Snackbar.LENGTH_SHORT).show()
    }
    fun Fragment.openSnackbar(text: Any) {
        Snackbar.make(requireView(), text.toString(), Snackbar.LENGTH_SHORT).show()
    }

    fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()

    fun Fragment.openTopSnackbar(text: String) {
        val snackbar = Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
        val layoutParams = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER
        snackbar.view.layoutParams = layoutParams
        snackbar.show()
    }
}