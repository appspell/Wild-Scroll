package com.appspell.wildscroll.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import com.appspell.wildscroll.sections.SectionInfo

abstract class SectionFastScrollAdapter<VH : ViewHolder> : RecyclerView.Adapter<VH>(), SectionFastScroll {

    var sections: Map<Int, SectionInfo> = emptyMap()
        set(value) {
            field = value
            value.values.forEach { section ->
                notifyItemChanged(section.position)
            }
        }

    open fun isSection(position: Int): Boolean = sections.containsKey(position)

    fun getSectionInfo(position: Int) = sections[position]
}