package com.appspell.wildscroll

import android.support.v7.widget.LinearSmoothScroller
import android.view.MotionEvent


class FastScroll(val recyclerView: WildScrollRecyclerView) {

    var sections: Sections? = null

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP ->
                if (sections!!.contains(ev.x, ev.y)) {
                    sections!!.selected = getSelectionSectionIndex(ev.y)
                    scrollToSection(sections!!.selected)
                    recyclerView.invalidateSectionBar()
                    return true
                }

//            MotionEvent.ACTION_MOVE ->
//                if (sections!!.contains(ev.x, ev.y)) {
//                    sections!!.selected = getSelectionSectionIndex(ev.y)
//                    val scrollProgress = getScrollProgress(ev.y)
//                    val itemPosition = Math.round(recyclerView.adapter.itemCount * scrollProgress)
//                    smoothScrollToPosition(itemPosition)
//                    recyclerView.invalidateSectionBar()
//                    return true
//                }
        }
        return false
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return sections!!.contains(ev.x, ev.y)
    }

    private fun getScrollProgress(y: Float): Float = y / recyclerView.height

    fun getSelectionSectionIndex(y: Float): Int {
        if (sections == null) return Sections.UNSELECTED
        return Math.floor((y * sections!!.getCount() / recyclerView.height).toDouble()).toInt()
    }

    private fun scrollToSection(sectionIndex: Int) {
        val position = sections!!.getSectionPositionByIndex(sectionIndex)
        smoothScrollToPosition(position!!)
    }

    private fun smoothScrollToPosition(itemPosition: Int) {
        smoothScroller.targetPosition = itemPosition
        recyclerView.layoutManager.startSmoothScroll(smoothScroller)
    }


    val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }

}