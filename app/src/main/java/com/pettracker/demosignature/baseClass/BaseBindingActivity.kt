package com.pettracker.demosignature.baseClass

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


abstract class BaseBindingActivity<VB : ViewBinding>(val bindingFactory: (LayoutInflater) -> VB) : ComponentActivity() {

    protected val binding: VB by lazy { bindingFactory(layoutInflater) }
    private val coroutineScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mContext = this

        initViews()
        initViewListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    abstract fun initViews()
    open fun initViewListener() { }

    fun launchCoroutine(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launch { block() }
    }
}

