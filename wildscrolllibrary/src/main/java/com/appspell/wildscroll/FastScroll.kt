package com.appspell.wildscroll

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MotionEvent


class FastScroll(val recyclerView: WildScrollRecyclerView,
                 var sections: Sections) {

    val scroller = object : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView!!.layoutManager

            val firstItemPosition = when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null)[0]
                else -> RecyclerView.NO_POSITION
            }

            sections.selected = getSectionIndexByScrollPosition(firstItemPosition)
        }
    }

    init {
        recyclerView.addOnScrollListener(scroller)
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP ->
                if (sections.contains(ev.x, ev.y)) {
                    val sectionIndex = getSelectionSectionIndex(ev.y)
                    val sectionInfo = sections.getSectionInfoByIndex(sectionIndex)

                    smoothScrollToPosition(sectionInfo!!.position)

                    sections.selected = sectionIndex
                    recyclerView.invalidateSectionBar()
                    return true
                }

            MotionEvent.ACTION_MOVE ->
                if (sections.contains(ev.x, ev.y)) {
                    val sectionIndex = getSelectionSectionIndex(ev.y)
                    val sectionInfo = sections.getSectionInfoByIndex(sectionIndex)!!

                    val dY = ev.y - sections.height * sectionIndex
                    val sectionProgress = dY / sections.height
                    val dPosition = sectionInfo.count * sectionProgress
                    val position = Math.round(sectionInfo.position + dPosition)

                    scrollToPosition(position)

                    sections.selected = sectionIndex
                    recyclerView.invalidateSectionBar()
                    return true
                }
        }
        return false
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return sections.contains(ev.x, ev.y)
    }

    private fun getSelectionSectionIndex(y: Float): Int {
        if (sections == null) return Sections.UNSELECTED
        return Math.floor((y * sections.getCount() / recyclerView.height).toDouble()).toInt()
    }

    private fun getSectionIndexByScrollPosition(itemPosition: Int): Int {
        if (sections == null || recyclerView.adapter !is SectionFastScroll) return Sections.UNSELECTED
        val sectionName = (recyclerView.adapter as SectionFastScroll).getSectionName(itemPosition)
        val sectionKey = sections.createShortName(sectionName)
        return sections.sections.indexOfKey(sectionKey)
    }

    private fun smoothScrollToPosition(itemPosition: Int) {
        smoothScroller.targetPosition = itemPosition
        recyclerView.layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun scrollToPosition(itemPosition: Int) {
        recyclerView.layoutManager.scrollToPosition(itemPosition)
    }

    private val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }
}