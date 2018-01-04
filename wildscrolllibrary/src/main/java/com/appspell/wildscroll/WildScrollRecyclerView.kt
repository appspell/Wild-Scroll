package com.appspell.wildscroll

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import appspell.com.wildscroll.R

class WildScrollRecyclerView : RecyclerView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val sections = Sections()
    private val fastScroll = FastScroll()

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
            this.selected = 1
            this.paddingLeft = paddingLeft
            this.paddingRight = paddingRight
        }

        with(fastScroll) {
            this.sections = sections
            this.recyclerView = this@WildScrollRecyclerView
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        refreshSectionsUI(width, height)
        super.onSizeChanged(width, height, oldw, oldh)
    }

    override fun onDraw(c: Canvas?) {
        super.onDraw(c)

        Log.d("FASTSCROLL", "onDraw")
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        Log.d("FASTSCROLL", "Draw")

        canvas!!
        if (showSections) {
            canvas.drawRect(sectionsRect, sectionsPaint)

            sections.sections.forEachIndexed { index, section ->
                when (sections.selected == index) {
                    true -> canvas.drawText(section, sections.left + sections.paddingLeft, sections.top + index * sections.height, textSelectedPaint)
                    false -> canvas.drawText(section, sections.left + sections.paddingLeft, sections.top + index * sections.height, textPaint)
                }
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {

        fastScroll.sections = sections //TODO
        if (fastScroll.onTouchEvent(ev)) {
            return true
        }

        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        fastScroll.sections = sections //TODO
        return if (showSections && fastScroll.onInterceptTouchEvent(ev)) true
        else super.onInterceptTouchEvent(ev)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        adapter?.registerAdapterDataObserver(DataObserver())
        super.setAdapter(adapter)
    }

    fun invalidateSectionBar() {
        invalidate(sectionsRect)
    }

    private fun refreshSectionsUI(width: Int, height: Int) {
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

    inner class DataObserver : AdapterDataObserver() {
        override fun onChanged() {
            refreshSectionsUI(width, height)
            super.onChanged()
        }
    }
}
