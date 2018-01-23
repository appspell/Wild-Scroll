package com.appspell.wildscroll.sections.fastscroll

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.HORIZONTAL
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MotionEvent
import appspell.com.wildscroll.R
import com.appspell.wildscroll.adapter.SectionFastScroll
import com.appspell.wildscroll.sections.SectionBarView
import com.appspell.wildscroll.sections.Sections


class FastScroll(private val sectionBar: SectionBarView) {

    private val sections
        get() = sectionBar.sections

    private val recyclerView
        get() = sectionBar.recyclerView

    private var isScrolling = false

    private var lastPositionX = 0f
    private var lastPositionY = 0f
    private val minScrollSensitivity: Float

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
        minScrollSensitivity = recyclerView.resources.getDimension(R.dimen.fastscroll_minimum_scrolling_sensitivity)
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                recyclerView.parent.requestDisallowInterceptTouchEvent(false)
                lastPositionX = ev.x
                lastPositionY = ev.y
                isScrolling = false
            }
            MotionEvent.ACTION_UP -> {
                recyclerView.parent.requestDisallowInterceptTouchEvent(false)
                sectionBar.dismissPopup()

                if (!isScrolling && sections.contains(ev.x, ev.y)) {
                    val sectionIndex = getSectionIndex(ev.y)
                    val sectionInfo = sections.getSectionInfoByIndex(sectionIndex)

                    scrollToPosition(sectionInfo!!.position) //TODO smooth scroll

                    sections.selected = sectionIndex
                    sectionBar.invalidateSectionBar()

                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                recyclerView.parent.requestDisallowInterceptTouchEvent(true)

                val dif = when (isHorizontalScroll()) {
                    true -> Math.abs(lastPositionX - ev.x) > minScrollSensitivity
                    false -> Math.abs(lastPositionY - ev.y) > minScrollSensitivity
                }

                if (dif && sections.contains(ev.x, ev.y)) {
                    val sectionIndex = getSectionIndex(ev.y)
                    val sectionInfo = sections.getSectionInfoByIndex(sectionIndex)!!

                    val dY = ev.y - sections.height * sectionIndex
                    val sectionProgress = dY / sections.height
                    val dPosition = sectionInfo.count * sectionProgress
                    val position = Math.round(sectionInfo.position + dPosition)

                    scrollToPosition(position)

                    sections.selected = sectionIndex
                    sectionBar.invalidateSectionBar()

                    sectionBar.showPopup(sectionInfo, ev.x.toInt(), ev.y.toInt())

                    isScrolling = true
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                recyclerView.parent.requestDisallowInterceptTouchEvent(false)
                sectionBar.dismissPopup()
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

    private fun getSectionIndex(pos: Float): Int =
            when (isHorizontalScroll()) {
                true -> Math.floor((pos * sections.getCount() / sectionBar.width).toDouble()).toInt()
                false -> Math.floor((pos * sections.getCount() / sectionBar.height).toDouble()).toInt()
            }

    private fun getSectionIndexByAdapterItemPosition(itemPosition: Int): Int {
        if (recyclerView.adapter !is SectionFastScroll || itemPosition == RecyclerView.NO_POSITION) return Sections.UNSELECTED
        val sectionName = (recyclerView.adapter as SectionFastScroll).getSectionName(itemPosition)
        val sectionKey = sections.createShortName(sectionName)
        return sections.sections.indexOfKey(sectionKey)
    }

    private fun scrollToPosition(itemPosition: Int) {
        recyclerView.stopScroll()
        when (recyclerView.layoutManager) {
            is LinearLayoutManager ->
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(itemPosition, 0)
            else ->
                recyclerView.layoutManager.scrollToPosition(itemPosition)
        }
    }

    private fun smoothScrollToPosition(itemPosition: Int) {
        recyclerView.stopScroll()
        smoothScroller.targetPosition = itemPosition
        if (!smoothScroller.isRunning) {
            recyclerView.layoutManager.startSmoothScroll(smoothScroller)
        }
    }

    private fun isHorizontalScroll(): Boolean =
            when (recyclerView.layoutManager) {
                is LinearLayoutManager ->
                    (recyclerView.layoutManager as LinearLayoutManager).orientation == HORIZONTAL
                is GridLayoutManager ->
                    (recyclerView.layoutManager as GridLayoutManager).orientation == HORIZONTAL
                is StaggeredGridLayoutManager ->
                    (recyclerView.layoutManager as StaggeredGridLayoutManager).orientation == HORIZONTAL
                else ->
                    false
            }

    private val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }
}