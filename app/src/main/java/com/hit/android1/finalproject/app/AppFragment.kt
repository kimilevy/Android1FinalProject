package com.hit.android1.finalproject.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class AppFragment<T: ViewBinding> : Fragment() {
    private lateinit var _view: View
    private var _binding: T? = null
    protected val binding get() = _binding!!

    protected open fun innerRunOnCreate() {}

    // Runs this function on create of the fragment (To use the onCreate view after the view was created)
    protected open fun _runOnCreateView(view: View) = runOnCreateView(view)
    protected open fun runOnCreateView(view: View) {}

    // Runs this function on create of the fragment (To use the onCreate view after the view was created)
    protected open fun _runOnDestroyView(view: View) = runOnDestroyView(view)
    protected open fun runOnDestroyView(view: View) {}

    // Abstract Functions
    abstract fun inflate(): T

    // Create with model
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate()
        _view = binding.root
        _runOnCreateView(_view)
        return _view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _runOnDestroyView(_view)
        _binding = null
    }
}