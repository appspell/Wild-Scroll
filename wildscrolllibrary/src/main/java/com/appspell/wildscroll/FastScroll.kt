package com.appspell.wildscroll

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent

class FastScroll {

    var sections: Sections? = null
    var recyclerView: RecyclerView? = null

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->
                if (contains(ev.x, ev.y)) {
                    val scroll = getSectionScroll(ev.y)
                    recyclerView!!.smoothScrollToPosition(scroll)
                    return true
                }
            MotionEvent.ACTION_MOVE -> {
                if (contains(ev.x, ev.y)) {
                    val scroll = getSectionScroll(ev.y)
                    recyclerView!!.smoothScrollToPosition(scroll)
                }
                return true
            }
        }
        return false
    }

    fun contains(x: Float, y: Float): Boolean {
        return x >= sections!!.offsetX &&
                x <= sections!!.offsetX + sections!!.width &&
                y >= sections!!.offsetY &&
                y <= sections!!.height * sections!!.sections.size
    }

    private fun getScroll(y: Float): Int {
        val scrollProgress = y / recyclerView!!.height
        return (recyclerView!!.adapter.itemCount * scrollProgress).toInt()
    }

    private fun getSectionScroll(y: Float): Int {
        val scrollProgress = y / recyclerView!!.height
        val sectionIndex = getSelectionSectionIndex(y)

        sections!!.section = sectionIndex

        return (recyclerView!!.adapter.itemCount * scrollProgress).toInt()
    }

    private fun getSelectionSectionIndex(y: Float): Int {
        val scrollProgress = y / recyclerView!!.height
        return Math.round(scrollProgress * sections!!.sections.size) - 1
    }
}