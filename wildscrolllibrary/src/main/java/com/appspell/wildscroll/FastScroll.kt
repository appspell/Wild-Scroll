package com.appspell.wildscroll

import android.util.Log
import android.view.MotionEvent

class FastScroll {

    var sections: Sections? = null
    var recyclerView: WildScrollRecyclerView? = null

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP ->
                if (sections!!.contains(ev.x, ev.y)) {
                    val scroll = getSectionScroll(ev.y)
                    Log.d("FASTSCROLL", "UP Scroll = $scroll")
                    recyclerView!!.smoothScrollToPosition(scroll)
                    recyclerView!!.invalidateSectionBar()
                    return true
                }

            MotionEvent.ACTION_MOVE ->
                if (sections!!.contains(ev.x, ev.y)) {
                    val scroll = getSectionScroll(ev.y)
                    Log.d("FASTSCROLL", "Move Scroll = $scroll")
                    recyclerView!!.smoothScrollToPosition(scroll)
                    recyclerView!!.invalidateSectionBar()
                    return true
                }
        }
        return false
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return sections!!.contains(ev.x, ev.y)
    }

    private fun getScroll(y: Float): Int {
        val scrollProgress = y / recyclerView!!.height
        return (recyclerView!!.adapter.itemCount * scrollProgress).toInt()
    }

    private fun getSectionScroll(y: Float): Int {
        val scrollProgress = y / recyclerView!!.height
        val sectionIndex = getSelectionSectionIndex(y)

        sections!!.selected = sectionIndex
        Log.d("FASTSCROLL", "Selected = ${sections!!.sections[sections!!.selected]}")


        return (recyclerView!!.adapter.itemCount * scrollProgress).toInt()
    }

    private fun getSelectionSectionIndex(y: Float): Int {
        val scrollProgress = y / recyclerView!!.height
        return Math.round(scrollProgress * sections!!.sections.size) - 1
    }
}