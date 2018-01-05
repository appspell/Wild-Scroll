package com.appspell.wildscroll

import android.view.Gravity

class Sections(val recyclerView: WildScrollRecyclerView) {

    companion object {
        const val UNSELECTED = -1;
    }

    var left = 0f
    var top = 0f
    var width = 0f
    var height = 0f
    var gravity = Gravity.RIGHT

    var paddingLeft = 200f
    var paddingRight = 20f

    lateinit var sections: List<String>

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

    fun refreshSections() {
        if (recyclerView.adapter == null) {
            return
        }
        if (recyclerView.adapter.itemCount <= 1 || recyclerView.adapter !is SectionFastScroll) {
            return
        }

        val list = ArrayList<String>()

        val adapter = recyclerView.adapter as SectionFastScroll

        if (recyclerView.adapter.itemCount > 0) {
            sections = (0..recyclerView.adapter.itemCount - 1)
                    .mapTo(list) { adapter.getSectionName(it)[0].toUpperCase().toString() }
                    .distinct()
        }
    }
}