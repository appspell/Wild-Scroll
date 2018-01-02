package com.appspell.wildscroll

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.GestureDetector
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
        val textColor = Color.LTGRAY
        val textSelectedColor = Color.RED

        val size = context.resources.getDimension(R.dimen.notification_action_text_size) //FIXME
        val selectedSize = context.resources.getDimension(R.dimen.notification_action_text_size)//FIXME

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
            color = Color.BLACK
        }

        with(sections) {
            this.section = 1
        }

        with(fastScroll) {
            this.sections = sections
            this.recyclerView = this@WildScrollRecyclerView
        }
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//
//        val desiredWidth = textSelectedPaint.textSize.toInt()
//
//        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
//        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
//        var width = 0
//
//        when (widthMode) {
//            MeasureSpec.EXACTLY -> width = widthSize
//            MeasureSpec.AT_MOST -> width = Math.min(desiredWidth, widthSize)
//            MeasureSpec.UNSPECIFIED -> width = desiredWidth
//        }
//
//        setMeasuredDimension(width, heightMeasureSpec)
//    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        refreshSectionsUI(width, height)
        super.onSizeChanged(width, height, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!
        if (showSections) {
            canvas.drawRect(sectionsRect, sectionsPaint)

            sections.sections.forEachIndexed { index, section ->
                when (sections.section == index) {
                    true -> canvas.drawText(section, sections.offsetX, sections.offsetY + index * sections.height, textSelectedPaint)
                    false -> canvas.drawText(section, sections.offsetX, sections.offsetY + index * sections.height, textPaint)
                }
            }
        }
    }

    private var mGestureDetector: GestureDetector? = null
    override fun onTouchEvent(ev: MotionEvent): Boolean {

        fastScroll.sections = sections
        if (fastScroll.onTouchEvent(ev)) {
            return true
        }

//        if (mGestureDetector == null) {
//            mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
//
//                override fun onFling(e1: MotionEvent, e2: MotionEvent,
//                                     velocityX: Float, velocityY: Float): Boolean {
//                    return super.onFling(e1, e2, velocityX, velocityY)
//                }
//
//            })
//        }
//        mGestureDetector!!.onTouchEvent(ev)

        return super.onTouchEvent(ev)
    }


//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
//            if (showSections && fastScroll.contains(ev.x, ev.y)) true
//            else super.onInterceptTouchEvent(ev)

    override fun setAdapter(adapter: Adapter<*>?) {
        adapter?.registerAdapterDataObserver(DataObserver())
        super.setAdapter(adapter)
    }

    private fun refreshSectionsUI(width: Int, height: Int) {
        sections.changeSize(width, height, textSelectedPaint.textSize)

        if (textPaint.textSize > sections.height) {
            textPaint.textSize = sections.height
        }

        if (textSelectedPaint.textSize > sections.height) {
            textSelectedPaint.textSize = sections.height
        }

        with(sectionsRect) {
            left = sections.offsetX.toInt()
            right = (sections.offsetX + sections.width).toInt()
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
