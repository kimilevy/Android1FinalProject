package com.hit.android1.finalproject.app

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.hit.android1.finalproject.app.Extensions.getOrCreate
import com.hit.android1.finalproject.app.Extensions.logDebug

abstract class AppFragmentWithModel<T: ViewBinding, S: ViewModel>(private val vm: Class<S>) : AppFragment<T>() {
    companion object {
        private var _viewModels = mutableMapOf<String, ViewModel>()
    }
    protected lateinit var viewModel: S

    // Runs this function on create of the fragment (To use the onCreate view after the view was created)
    override fun _runOnCreateView(view: View) {
        vm.canonicalName?.let {
            viewModel = _viewModels.getOrCreate(it){ ViewModelProvider(this).get(vm) } as S
            logDebug(_viewModels.keys.toString())
        }
        runOnCreateView(view)
    }
}