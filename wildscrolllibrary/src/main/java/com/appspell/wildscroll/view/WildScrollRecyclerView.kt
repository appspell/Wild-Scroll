package com.appspell.wildscroll.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import appspell.com.wildscroll.R
import com.appspell.wildscroll.fastscroll.FastScroll
import com.appspell.wildscroll.fastscroll.OnSectionChangedListener
import com.appspell.wildscroll.fastscroll.Sections

class WildScrollRecyclerView : RecyclerView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val sections = Sections(this)
    private val fastScroll = FastScroll(this, sections)

    private var showSections = true
    private val textPaint = Paint()
    private val textSelectedPaint = Paint()
    private val sectionsPaint = Paint()
    private val sectionsRect = Rect()

    init {
        val textColor = ResourcesCompat.getColor(context.resources, R.color.primary_material_dark, null) //FIXME
        val textSelectedColor = ResourcesCompat.getColor(context.resources, R.color.accent_material_dark, null) //FIXME
        val backgroundColor = ResourcesCompat.getColor(context.resources, R.color.ripple_material_light, null) //FIXME

        val size = context.resources.getDimension(R.dimen.notification_action_text_size) //FIXME
        val selectedSize = context.resources.getDimension(R.dimen.notification_action_text_size)//FIXME

        val paddingLeft = context.resources.getDimension(R.dimen.abc_button_padding_horizontal_material)//FIXME
        val paddingRight = context.resources.getDimension(R.dimen.abc_button_padding_horizontal_material)//FIXME

        val textTypeFace = Typeface.DEFAULT
        val textSelectedTypeFace = Typeface.DEFAULT

        with(textPaint) {
            color = textColor
            isAntiAlias = true
            textSize = size
            typeface = textTypeFace
        }

        with(textSelectedPaint) {
            color = textSelectedColor
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
        }

        with(fastScroll) {
            this.sections = sections
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

            when (sections.selected == index) {
                true -> canvas.drawText(section.key.toString(), posX, top + textSelectedPaint.textSize / 2, textSelectedPaint)
                false -> canvas.drawText(section.key.toString(), posX, top + textPaint.textSize / 2, textPaint)
            }
        }
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


    //Memory leaks (!)
    val dataObserver = object : AdapterDataObserver() {
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
