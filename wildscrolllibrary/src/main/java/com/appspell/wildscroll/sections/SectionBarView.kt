package com.appspell.wildscroll.sections

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Rect
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import com.appspell.wildscroll.sections.Gravity.LEFT
import com.appspell.wildscroll.sections.Gravity.RIGHT
import com.appspell.wildscroll.sections.fastscroll.FastScroll
import com.appspell.wildscroll.sections.popup.SectionCirclePopup
import com.appspell.wildscroll.sections.popup.SectionPopup
import com.appspell.wildscroll.view.WildScrollRecyclerView

class SectionBarView(val recyclerView: WildScrollRecyclerView) {

    @ColorInt
    var textColor: Int = 0
        set(value) {
            field = value
            textPaint.color = value
        }

    @ColorInt
    var highlightColor: Int = 0
        set(value) {
            field = value
            highlightTextPaint.color = value
        }

    @ColorInt
    var sectionBarBackgroundColor: Int = 0
        set(value) {
            field = value
            sectionsPaint.color = value
        }

    var sectionBarPaddingLeft: Float = 0f
        set(value) {
            field = value
            sections.paddingLeft = value
            onSizeChanged(recyclerView.width, recyclerView.height)
        }

    var sectionBarPaddingRight: Float = 0f
        set(value) {
            field = value
            sections.paddingRight = value
            onSizeChanged(recyclerView.width, recyclerView.height)
        }

    var textSize: Float = 0f
        set(value) {
            field = value
            textPaint.textSize = value
            onSizeChanged(recyclerView.width, recyclerView.height)
        }

    var highlightTextSize: Float = 0f
        set(value) {
            field = value
            highlightTextPaint.textSize = value
            onSizeChanged(recyclerView.width, recyclerView.height)
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

    var sectionBarCollapseDigital: Boolean = true
        set(value) {
            field = value
            sections.collapseDigital = value
        }

    var sectionBarGravity: Gravity = Gravity.RIGHT
        set(value) {
            field = value
            sections.gravity = value
            onSizeChanged(recyclerView.width, recyclerView.height)
        }

    var sectionPopup: SectionPopup = SectionCirclePopup(context)
        set(value) {
            field = value
            field.onDismissListener = { invalidateSectionPopup() }
        }

    var sectionBarEnable: Boolean = true

    var popupEnable: Boolean = true

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
        with(textPaint) {
            isAntiAlias = true
            textAlign = Align.CENTER
        }

        with(highlightTextPaint) {
            isAntiAlias = true
            textAlign = Align.CENTER
        }
    }

    fun draw(canvas: Canvas) {
        if (sectionBarEnable == false || sections.getCount() == 0) {
            return
        }

        canvas.drawRect(sectionsRect, sectionsPaint)

        when (sections.gravity) {
        //TODO draw top and bottom
            Gravity.LEFT, Gravity.RIGHT -> {
                val posX = (sections.left + sections.width / 2f)

                sections.sections.entries.forEachIndexed { index, section ->
                    val top = sections.top + (index + 1) * sections.height - sections.height / 2f

                    //TODO implement gravity top or bottom (horizontal)
                    when (sections.selected == index) {
                        true -> canvas.drawText(section.key.toString(), posX, top + highlightTextPaint.textSize / 2f, highlightTextPaint)
                        false -> canvas.drawText(section.key.toString(), posX, top + textPaint.textSize / 2f, textPaint)
                    }
                }
            }
        }

        if (popupEnable) {
            sectionPopup.draw(canvas)
        }
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        return fastScroll.onTouchEvent(ev)
    }

    fun onInterceptTouchEvent(ev: MotionEvent): Boolean = sectionBarEnable && fastScroll.onInterceptTouchEvent(ev)

    fun onSizeChanged(width: Int, height: Int) {
        if (width == 0 && height == 0) return

        val maxTextSize = Math.max(textPaint.textSize, highlightTextPaint.textSize)
        sections.changeSize(width, height, maxTextSize)

        when (sections.gravity) {
        //TODO top and bottom
            LEFT, RIGHT -> {
                if (textPaint.textSize > sections.width) {
                    textPaint.textSize = sections.width
                }

                if (highlightTextPaint.textSize > sections.width) {
                    highlightTextPaint.textSize = sections.width
                }

                with(sectionsRect) {
                    left = sections.left.toInt()
                    right = (sections.left + sections.width).toInt()
                    top = 0
                    bottom = height
                }
            }
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
        if (popupEnable == false) {
            return
        }

        val sectionName = section.shortName.toString()
        sectionPopup.show(sectionName, x, y, recyclerView.width, recyclerView.height, sections)
    }

    fun dismissPopup() {
        sectionPopup.dismiss()
    }

    fun invalidateLayout(adapter: RecyclerView.Adapter<*>?) {
        sections.refresh(adapter, object : OnSectionChangedListener { //TODO check memory leaks here
            override fun onSectionChanged() {
                onSizeChanged(recyclerView.width, recyclerView.height)
                invalidateSectionBar()
            }
        })
    }
}