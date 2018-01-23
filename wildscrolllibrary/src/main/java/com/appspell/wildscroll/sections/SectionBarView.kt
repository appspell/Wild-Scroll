package com.appspell.wildscroll.sections

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.MotionEvent
import appspell.com.wildscroll.R
import com.appspell.wildscroll.sections.fastscroll.FastScroll
import com.appspell.wildscroll.sections.popup.SectionCirclePopup
import com.appspell.wildscroll.sections.popup.SectionPopup
import com.appspell.wildscroll.view.WildScrollRecyclerView

class SectionBarView(val recyclerView: WildScrollRecyclerView) {

    @ColorRes
    var textColor: Int = R.color.fastscroll_default_text
        set(value) {
            field = value
            textPaint.color = ResourcesCompat.getColor(resources, value, context.theme)
        }

    @ColorRes
    var highlightColor: Int = R.color.fastscroll_highlight_text
        set(value) {
            field = value
            highlightTextPaint.color = ResourcesCompat.getColor(resources, value, context.theme)
        }

    @ColorRes
    var sectionBackgroundColor: Int = R.color.fastscroll_section_background
        set(value) {
            field = value
            sectionsPaint.color = ResourcesCompat.getColor(resources, value, context.theme)
        }

    @DimenRes
    var paddingLeft: Int = R.dimen.fastscroll_section_padding
        set(value) {
            field = value
            sections.paddingLeft = resources.getDimension(value)
        }

    @DimenRes
    var paddingRight: Int = R.dimen.fastscroll_section_padding
        set(value) {
            field = value
            sections.paddingLeft = resources.getDimension(value)
        }

    @DimenRes
    var textSize: Int = R.dimen.fastscroll_section_text_size
        set(value) {
            field = value
            textPaint.textSize = resources.getDimension(value)
        }

    @DimenRes
    var highlightTextSize: Int = R.dimen.fastscroll_section_highlight_text_size
        set(value) {
            field = value
            highlightTextPaint.textSize = resources.getDimension(value)
        }

    var textTypeFace: Typeface = Typeface.DEFAULT
        set(value) {
            field = value
            textPaint.typeface = value
        }

    var highlightTextFace: Typeface = Typeface.DEFAULT
        set(value) {
            field = value
            highlightTextPaint.typeface = value
        }

    var collapseDigital: Boolean = true
        set(value) {
            field = value
            sections.collapseDigital = value
            invalidateLayout(recyclerView.width, recyclerView.height, recyclerView.adapter) //TODO
        }

    var gravity: Int = Gravity.RIGHT
        set(value) {
            field = value
            sections.gravity = value
        }

    var sectionPopup: SectionPopup = SectionCirclePopup(context)
        set(value) {
            field = value
            field.onDismissListener = { invalidateSectionPopup() }
        }

    var enable: Boolean = true

    var showPopup: Boolean = true

    val width: Int
        get() = sectionsRect.right - sectionsRect.left

    val height: Int
        get() = sectionsRect.bottom - sectionsRect.top

    var sections: Sections = Sections()

    private val fastScroll = FastScroll(this)

    private val textPaint = Paint()
    private val highlightTextPaint = Paint()
    private val sectionsPaint = Paint()
    private val sectionsRect = Rect()

    private val context
        get() = recyclerView.context
    private val resources
        get() = context.resources

    init {
        textPaint.run {
            isAntiAlias = true
        }

        highlightTextPaint.run {
            isAntiAlias = true
        }
    }

    fun draw(canvas: Canvas) {
        if (enable == false || sections.getCount() == 0) {
            return
        }

        canvas.drawRect(sectionsRect, sectionsPaint)

        val posX = sections.left + sections.paddingLeft

        sections.sections.entries.forEachIndexed { index, section ->
            val top = sections.top + (index + 1) * sections.height - sections.height / 2

            //TODO implement gravity top or bottom
            when (sections.selected == index) {
                true -> canvas.drawText(section.key.toString(), posX, top + highlightTextPaint.textSize / 2, highlightTextPaint)
                false -> canvas.drawText(section.key.toString(), posX, top + textPaint.textSize / 2, textPaint)
            }
        }

        if (showPopup) {
            sectionPopup.draw(canvas)
        }
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        return fastScroll.onTouchEvent(ev)
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean = enable && fastScroll.onInterceptTouchEvent(ev)

    fun onSizeChanged(width: Int, height: Int) {
        sections.changeSize(width, height, highlightTextPaint.textSize)

        if (sections.height > 0) {
            if (textPaint.textSize > sections.height) {
                textPaint.textSize = sections.height
            }

            if (highlightTextPaint.textSize > sections.height) {
                highlightTextPaint.textSize = sections.height
            }
        }

        sectionsRect.run {
            left = sections.left.toInt()
            right = (sections.left + sections.width).toInt()
            top = 0
            bottom = height
        }
    }

    fun invalidateSectionBar() {
        fastScroll.selectSectionByFirstVisibleItem()
        recyclerView.invalidate(sectionsRect)
    }

    private fun invalidateSectionPopup() {
        recyclerView.invalidate(sectionPopup.getRect())
    }

    fun showPopup(section: SectionInfo, x: Int, y: Int) {
        if (showPopup == false) {
            return
        }
        var popupX = x
        var popupY = y
        val sectionName = section.shortName.toString()

        when (sections.gravity) {
            Gravity.START, Gravity.LEFT -> {
                popupX = sections.width.toInt() + sections.width.toInt()
                popupY = y - sectionPopup.height / 2
            }
            Gravity.END, Gravity.RIGHT -> {
                popupX = (sections.left - sectionPopup.width).toInt() - sections.width.toInt()
                popupY = y - sectionPopup.height / 2
            }
        //TODO top / bottom
        }

        //corrections
        if (popupX < 0) popupX = 0
        else if (popupX > width - sectionPopup.width) popupY = width - sectionPopup.width
        if (popupY < 0) popupY = 0
        else if (popupY > height - sectionPopup.height) popupY = height - sectionPopup.height

        sectionPopup.show(sectionName, popupX, popupY)
    }

    fun dismissPopup() {
        sectionPopup.dismiss()
    }

    fun invalidateLayout(width: Int, height: Int, adapter: RecyclerView.Adapter<*>?) {
        sections.refresh(adapter, object : OnSectionChangedListener { //TODO check memory leaks here
            override fun onSectionChanged() {
                onSizeChanged(width, height)
                invalidateSectionBar()
            }
        })
    }
}