package com.appspell.wildscroll

import android.support.v4.util.ArrayMap
import android.view.Gravity

class Sections(val recyclerView: WildScrollRecyclerView) {

    companion object {
        const val UNSELECTED = -1
    }

    var left = 0f
    var top = 0f
    var width = 0f
    var height = 0f
    var gravity = Gravity.RIGHT

    var paddingLeft = 200f
    var paddingRight = 20f

    lateinit var sections: ArrayMap<String, Int>

    var selected = UNSELECTED

    fun getCount() = sections.size

    fun changeSize(w: Int, h: Int, selectedTextSize: Float) {
        val sectionCount = sections.size
        width = selectedTextSize + paddingLeft + paddingRight
        height = h / sectionCount.toFloat()

        when (gravity) {
            Gravity.LEFT -> left = 0f
            Gravity.START -> left = 0f
            Gravity.RIGHT -> left = w - width
            Gravity.END -> left = w - width
        }

//        top = height
    }

    fun contains(x: Float, y: Float): Boolean {
        return x >= left &&
                x <= left + width &&
                y >= top &&
                y <= height * sections.size
    }


    fun getSectionByIndex(index: Int): String = sections.keyAt(index)

    fun getSectionPositionByIndex(index: Int): Int? {
        val key = getSectionByIndex(index)
        return sections[key]
    }

    fun refreshSections() {
        if (recyclerView.adapter == null) {
            return
        }
        if (recyclerView.adapter.itemCount <= 1 || recyclerView.adapter !is SectionFastScroll) {
            return
        }

        val map = ArrayMap<String, Int>()

        val adapter = recyclerView.adapter as SectionFastScroll

        if (recyclerView.adapter.itemCount > 0) {
            for (position in 0 until recyclerView.adapter.itemCount) {
                val section = adapter.getSectionName(position)[0].toUpperCase().toString()
                if (!map.contains(section)) {
                    map.put(section, position)
                }
            }
            sections = map
        }
    }


}