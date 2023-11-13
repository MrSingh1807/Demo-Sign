package com.pettracker.demosignature.baseClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.pettracker.demosignature.utils.ResponseDiffUtils

abstract class BaseBindingAdapter<T, VB : ViewBinding>(
    private val layoutInflater: (LayoutInflater, ViewGroup, Boolean) -> VB
) : RecyclerView.Adapter<BaseBindingAdapter<T, VB>.ViewHolder>() {
    private var itemList = emptyList<T>()

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = layoutInflater(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder, itemList[position], position)
    }

    abstract fun bind(holder: ViewHolder, item: T, position: Int)

    fun setData(newData: List<T>) {
        val diffUtilCallback = ResponseDiffUtils(itemList, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        itemList = newData
        diffResult.dispatchUpdatesTo(this)
    }
}
