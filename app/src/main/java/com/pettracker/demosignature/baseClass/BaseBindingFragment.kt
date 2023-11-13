package com.pettracker.demosignature.baseClass

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding


abstract class BaseBindingFragment<VB : ViewBinding, VM : ViewModel>(
    private val layoutInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val getViewModelClass: Class<VM>
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    protected lateinit var mViewModel: VM

    private var mLastClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = layoutInflater(inflater, container, false)
        mViewModel = ViewModelProvider(this)[getViewModelClass]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewListener()
    }

    open fun initViews() {}
    open fun initViewListener() {}

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun revokeClickForOneSecond() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

}

