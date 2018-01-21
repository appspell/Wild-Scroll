package com.appspell.wildscroll.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.annotation.ColorRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import appspell.com.wildscroll.R
import com.appspell.wildscroll.fastscroll.FastScroll
import com.appspell.wildscroll.sections.OnSectionChangedListener
import com.appspell.wildscroll.sections.Sections
import com.appspell.wildscroll.sections.popup.SectionCirclePopup
import com.appspell.wildscroll.sections.popup.SectionPopup

class WildScrollRecyclerView : RecyclerView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @ColorRes
    var sectionTextColor = R.color.fastscroll_default_text
        set(value) {
            field = value
            textPaint.color = ResourcesCompat.getColor(context.resources, value, context.theme)
        }

    @ColorRes
    var sectionHighlightColor = R.color.fastscroll_highlight_text
        set(value) {
            field = value
            textSelectedPaint.color = ResourcesCompat.getColor(context.resources, value, context.theme)
        }

    @ColorRes
    var sectionBackgroundColor = R.color.fastscroll_section_background
        set(value) {
            field = value
            sectionsPaint.color = ResourcesCompat.getColor(context.resources, value, context.theme)
        }

    var popupSection: SectionPopup
        set(value) {
            field = value
            field.sections = sections
            fastScroll.sectionPopup = field
        }

    private val sections: Sections
    private val fastScroll: FastScroll

    private var showSections = true
    private val textPaint = Paint()
    private val textSelectedPaint = Paint()
    private val sectionsPaint = Paint()
    private val sectionsRect = Rect()

    init {
        sections = Sections(this)
        fastScroll = FastScroll(this, sections)
        popupSection = SectionCirclePopup()

        val textColor = ResourcesCompat.getColor(context.resources, sectionTextColor, context.theme) //FIXME
        val textHighlightColor = ResourcesCompat.getColor(context.resources, sectionHighlightColor, context.theme) //FIXME
        val backgroundColor = ResourcesCompat.getColor(context.resources, sectionBackgroundColor, context.theme) //FIXME

        val size = context.resources.getDimension(R.dimen.fastscroll_section_text_size) //FIXME
        val selectedSize = context.resources.getDimension(R.dimen.fastscroll_section_highlight_text_size)//FIXME

        val paddingLeft = context.resources.getDimension(R.dimen.fastscroll_section_padding)//FIXME
        val paddingRight = context.resources.getDimension(R.dimen.fastscroll_section_padding)//FIXME

        val textTypeFace = Typeface.DEFAULT
        val textSelectedTypeFace = Typeface.DEFAULT

        with(textPaint) {
            color = textColor
            isAntiAlias = true
            textSize = size
            typeface = textTypeFace
        }

        with(textSelectedPaint) {
            color = textHighlightColor
            isAntiAlias = true
            textSize = selectedSize
            typeface = textSelectedTypeFace
        }

        with(sectionsPaint) {
            color = backgroundColor
        }

        with(sections) {
            this.paddingLeft = paddingLeft
            this.paddingRight = paddingRight
            this.collapseDigital = true //FIXME
            this.gravity = Gravity.RIGHT
        }

        with(popupSection) {
            this.sections = sections
            this.recyclerView = this@WildScrollRecyclerView
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        refreshSectionsBarView(width, height)
        super.onSizeChanged(width, height, oldw, oldh)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        drawSectionBar(canvas!!)
    }

    private fun drawSectionBar(canvas: Canvas) {
        if (!showSections || sections.getCount() == 0) {
            return
        }

        canvas.drawRect(sectionsRect, sectionsPaint)

        val posX = sections.left + sections.paddingLeft

        sections.sections.entries.forEachIndexed { index, section ->
            val top = sections.top + (index + 1) * sections.height - sections.height / 2

            //TODO implement gravity top or bottom
            when (sections.selected == index) {
                true -> canvas.drawText(section.key.toString(), posX, top + textSelectedPaint.textSize / 2, textSelectedPaint)
                false -> canvas.drawText(section.key.toString(), posX, top + textPaint.textSize / 2, textPaint)
            }
        }

        popupSection.draw(canvas)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (fastScroll.onTouchEvent(ev)) {
            return true
        }
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (showSections && fastScroll.onInterceptTouchEvent(ev)) true
        else super.onInterceptTouchEvent(ev)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        adapter?.registerAdapterDataObserver(dataObserver)
        super.setAdapter(adapter)
    }

    fun invalidateSectionBar() {
        fastScroll.selectSectionByFirstVisibleItem()
        invalidate(sectionsRect)
    }

    fun release() {
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(dataObserver)
        }
    }

    private fun refreshSectionsBarView(width: Int, height: Int) {
        sections.changeSize(width, height, textSelectedPaint.textSize)

        if (sections.height > 0) {
            if (textPaint.textSize > sections.height) {
                textPaint.textSize = sections.height
            }

            if (textSelectedPaint.textSize > sections.height) {
                textSelectedPaint.textSize = sections.height
            }
        }

        with(sectionsRect) {
            left = sections.left.toInt()
            right = (sections.left + sections.width).toInt()
            top = 0
            bottom = height
        }
    }

    //TODO Memory leaks (!?)
    private val dataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            sections.refresh(object : OnSectionChangedListener {
                override fun onSectionChanged() {
                    refreshSectionsBarView(width, height)
                    invalidateSectionBar()
                }
            })
            super.onChanged()
        }
    }
}
