package com.testingpersonality.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.testingpersonality.R
import com.testingpersonality.utils.OneShot
import kotlinx.android.synthetic.main.category_item_row_layout.view.*

class SelectCategoryAdapter :
    RecyclerView.Adapter<SelectCategoryAdapter.ViewHolder>() {
    private var categoryList = listOf<String>()
    private var onClickItem = MutableLiveData<OneShot<String>>()
    var itemClick: LiveData<OneShot<String>> = onClickItem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_item_row_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chapterName.text = categoryList[position]
        holder.itemView.setOnClickListener {
            onClickItem.value = OneShot(categoryList[position])
        }
    }

    fun updateCategory(categoryItems: List<String>){
        categoryList = categoryItems
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chapterName: TextView = view.category_title
    }
}