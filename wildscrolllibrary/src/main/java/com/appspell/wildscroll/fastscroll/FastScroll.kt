package com.appspell.wildscroll.fastscroll

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MotionEvent
import com.appspell.wildscroll.adapter.SectionFastScroll
import com.appspell.wildscroll.sections.SectionPopup
import com.appspell.wildscroll.sections.Sections
import com.appspell.wildscroll.view.WildScrollRecyclerView


class FastScroll(private val recyclerView: WildScrollRecyclerView,
                 private val sections: Sections) {
    lateinit var sectionPopup: SectionPopup
    var isScrolling = false

    private val scroller: OnScrollListener = object : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isScrolling) {
                return
            }
            selectSectionByFirstVisibleItem()
        }
    }

    init {
        recyclerView.addOnScrollListener(scroller)
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->
                isScrolling = sections.contains(ev.x, ev.y)

            MotionEvent.ACTION_UP -> {
                sectionPopup.dismiss()

                if (sections.contains(ev.x, ev.y)) {
                    val sectionIndex = getSectionIndex(ev.y)
                    val sectionInfo = sections.getSectionInfoByIndex(sectionIndex)

                    scrollToPosition(sectionInfo!!.position) //TODO smooth scroll

                    sections.selected = sectionIndex
                    recyclerView.invalidateSectionBar()

                    return true
                }
            }
            MotionEvent.ACTION_MOVE ->
                if (sections.contains(ev.x, ev.y)) {
                    val sectionIndex = getSectionIndex(ev.y)
                    val sectionInfo = sections.getSectionInfoByIndex(sectionIndex)!!

                    val dY = ev.y - sections.height * sectionIndex
                    val sectionProgress = dY / sections.height
                    val dPosition = sectionInfo.count * sectionProgress
                    val position = Math.round(sectionInfo.position + dPosition)

                    scrollToPosition(position)

                    sections.selected = sectionIndex
                    recyclerView.invalidateSectionBar()

                    sectionPopup.show(sectionInfo, ev.x.toInt(), ev.y.toInt())
                    return true
                }
        }
        return false
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return sections.contains(ev.x, ev.y)
    }

    fun selectSectionByFirstVisibleItem() {
        val layoutManager = recyclerView.layoutManager

        val firstItemPosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null)[0]
            else -> RecyclerView.NO_POSITION
        }

        sections.selected = getSectionIndexByAdapterItemPosition(firstItemPosition)
    }

    private fun getSectionIndex(y: Float): Int {
        return Math.floor((y * sections.getCount() / recyclerView.height).toDouble()).toInt()
    }

    private fun getSectionIndexByAdapterItemPosition(itemPosition: Int): Int {
        if (recyclerView.adapter !is SectionFastScroll || itemPosition == RecyclerView.NO_POSITION) return Sections.UNSELECTED
        val sectionName = (recyclerView.adapter as SectionFastScroll).getSectionName(itemPosition)
        val sectionKey = sections.createShortName(sectionName)
        return sections.sections.indexOfKey(sectionKey)
    }

    private fun scrollToPosition(itemPosition: Int) {
        when (recyclerView.layoutManager) {
            is LinearLayoutManager ->
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(itemPosition, 0)
            else ->
                recyclerView.layoutManager.scrollToPosition(itemPosition)
        }
    }

    private fun smoothScrollToPosition(itemPosition: Int) {
        smoothScroller.targetPosition = itemPosition
        if (!smoothScroller.isRunning) {
            recyclerView.layoutManager.startSmoothScroll(smoothScroller)
        }
    }

    private val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }
}