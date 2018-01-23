package com.appspell.wildscroll.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import appspell.com.wildscroll.R
import com.appspell.wildscroll.sections.SectionBarView
import com.appspell.wildscroll.sections.popup.SectionCirclePopup

class WildScrollRecyclerView : RecyclerView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val sectionBar = SectionBarView(this)

    init {
        sectionBar.run {
            textColor = R.color.fastscroll_default_text
            textSize = R.dimen.fastscroll_section_text_size
            textTypeFace = Typeface.DEFAULT

            highlightColor = R.color.fastscroll_highlight_text
            highlightTextSize = R.dimen.fastscroll_section_highlight_text_size
            highlightTextFace = Typeface.DEFAULT

            sectionBackgroundColor = R.color.fastscroll_section_background
            paddingLeft = R.dimen.fastscroll_section_padding
            paddingRight = R.dimen.fastscroll_section_padding

            collapseDigital = true
            gravity = Gravity.RIGHT
            enable = true
            showPopup = true
            sectionPopup = SectionCirclePopup(context)
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        sectionBar.onSizeChanged(width, height)
        super.onSizeChanged(width, height, oldw, oldh)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        sectionBar.draw(canvas!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (sectionBar.onTouchEvent(ev)) {
            return true
        }
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
            if (sectionBar.onInterceptTouchEvent(ev)) true
            else super.onInterceptTouchEvent(ev)

    override fun setAdapter(adapter: Adapter<*>?) {
        adapter?.registerAdapterDataObserver(dataObserver)
        super.setAdapter(adapter)
    }

    fun release() {
        adapter?.unregisterAdapterDataObserver(dataObserver)
    }

    private val dataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            sectionBar.invalidateLayout(width, height, adapter)
            super.onChanged()
        }
    }
}
