package com.appspell.wildscroll

import android.view.MotionEvent

class FastScroll(val recyclerView: WildScrollRecyclerView) {

    var sections: Sections? = null

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP ->
                if (sections!!.contains(ev.x, ev.y)) {
                    val scroll = getSectionScroll(ev.y)
                    recyclerView.smoothScrollToPosition(scroll)
                    recyclerView.invalidateSectionBar()
                    return true
                }

            MotionEvent.ACTION_MOVE ->
                if (sections!!.contains(ev.x, ev.y)) {
                    val scroll = getSectionScroll(ev.y)
                    recyclerView.smoothScrollToPosition(scroll)
                    recyclerView.invalidateSectionBar()
                    return true
                }
        }
        return false
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return sections!!.contains(ev.x, ev.y)
    }

    private fun getScroll(y: Float): Int {
        val scrollProgress = y / recyclerView.height
        return (recyclerView.adapter.itemCount * scrollProgress).toInt()
    }

    private fun getSectionScroll(y: Float): Int {
        val scrollProgress = y / recyclerView.height
        val sectionIndex = getSelectionSectionIndex(y)

        return (recyclerView.adapter.itemCount * scrollProgress).toInt()
    }

    private fun getScrollProgress(y: Float): Float = y / recyclerView.height

    fun getSelectionSectionIndex(y: Float): Int {
        if (sections == null) return Sections.UNSELECTED
        return Math.floor((y * sections!!.getCount() / recyclerView.height).toDouble()).toInt()
    }
}