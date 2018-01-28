package com.appspell.wildscroll.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appspell.wildscroll.R
import com.appspell.wildscroll.data.Company
import com.appspell.wildscroll.sections.SectionInfo

class SectionsAdapter : SectionFastScrollAdapter<RecyclerView.ViewHolder>(), SectionFastScroll {
    companion object {

        const val SECTION = 10
        const val ITEM = 20
    }

    var items = emptyList<Company>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.count()

    override fun getItemViewType(position: Int): Int =
            if (isSection(position)) {
                SECTION
            } else {
                ITEM
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? =
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            SECTION -> {
                holder as SectionsSectionViewHolder
                holder.bind(getSectionInfo(position)!!)
            }
            ITEM -> {
                holder as SectionsItemViewHolder
                holder.bind(items[position])
            }
            else -> throw Exception("Cannot find binding for ${getItemViewType(position)}")
        }

    }

    override fun getSectionName(position: Int): String = items[position].company
}


class SectionsSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val section = itemView.findViewById<TextView>(R.id.section)

    fun bind(sectionInfo: SectionInfo) {
        section.text = sectionInfo.shortName.toString()
    }
}

class SectionsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val section = itemView.findViewById<TextView>(R.id.section)

    private val company = itemView.findViewById<TextView>(R.id.company)
    private val phrase = itemView.findViewById<TextView>(R.id.phrase)
    private val street = itemView.findViewById<TextView>(R.id.street)

    fun bind(item: Company) {
        section.text = item.company
        company.text = item.company
        phrase.text = item.phrase
        street.text = item.street
    }
}