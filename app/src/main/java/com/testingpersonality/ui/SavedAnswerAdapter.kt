package com.testingpersonality.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testingpersonality.R
import com.testingpersonality.data.local.PersonalityData
import kotlinx.android.synthetic.main.category_item_row_layout.view.*

class SavedAnswerAdapter :
    RecyclerView.Adapter<SavedAnswerAdapter.ViewHolder>() {
    private var savedList = listOf<PersonalityData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_item_row_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return savedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemText.text = holder.itemView.context.getString(R.string.question_answer_txt,
            position+1,savedList[position].question,savedList[position].option)
    }

    fun updateCategory(dataItems: List<PersonalityData>){
        savedList = dataItems
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.category_title
    }
}