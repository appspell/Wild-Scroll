package com.appspell.wildscroll.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appspell.wildscroll.R
import com.appspell.wildscroll.data.Company

class SampleSecondAdapter : RecyclerView.Adapter<SecondListViewHolder>(), SectionFastScroll {
    var items = emptyList<Company>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondListViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_second_sample_list, parent, false)
        return SecondListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SecondListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getSectionName(position: Int): String = items[position].company
}

class SecondListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val company: TextView = itemView.findViewById(R.id.company)
    private val phrase: TextView = itemView.findViewById(R.id.phrase)
    private val street: TextView = itemView.findViewById(R.id.street)

    fun bind(item: Company) {
        company.text = item.company
        phrase.text = item.phrase
        street.text = item.street
    }
}