package com.appspell.wildscroll.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appspell.wildscroll.R
import com.appspell.wildscroll.data.Company

class SectionsAdapter : RecyclerView.Adapter<SectionViewHolder>(), SectionFastScroll {

    companion object {
        const val SECTION = 10
        const val ITEM = 20
    }

    var items = emptyList<Company>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int =
            if (position == 0 || getSectionName(position) != getSectionName(position - 1)) {
                SECTION
            } else {
                ITEM
            }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder? =
            when (viewType) {
                SECTION -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sections_section, parent, false)
                    SectionsSectionViewHolder(view)
                }
                ITEM -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sections_item, parent, false)
                    SectionsItemViewHolder(view)
                }
                else -> null
            }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getSectionName(position: Int): String = items[position].company.sectionName()
}

abstract class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Company)
}

class SectionsSectionViewHolder(itemView: View) : SectionViewHolder(itemView) {
    private val section = itemView.findViewById<TextView>(R.id.section)

    override fun bind(item: Company) {
        section.text = item.company.sectionName()
    }
}

class SectionsItemViewHolder(itemView: View) : SectionViewHolder(itemView) {
    private val section = itemView.findViewById<TextView>(R.id.section)

    private val company = itemView.findViewById<TextView>(R.id.company)
    private val phrase = itemView.findViewById<TextView>(R.id.phrase)
    private val street = itemView.findViewById<TextView>(R.id.street)

    override fun bind(item: Company) {
        section.text = item.company.sectionName()
        company.text = item.company
        phrase.text = item.phrase
        street.text = item.street
    }
}

private fun String.sectionName(): String =
        if (this.isNotEmpty()) {
            capitalize()[0].toString()
        } else {
            ""
        }

