package com.appspell.wildscroll.sections

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import appspell.com.wildscroll.R

interface SectionPopup {
    fun show(section: SectionInfo, x: Int, y: Int)
    fun dismiss()
    fun draw(canvas: Canvas)
    var recyclerView: RecyclerView?
    var sections: Sections?
}

class StubSectionPopup : SectionPopup {
    override fun show(section: SectionInfo, x: Int, y: Int) {}
    override fun dismiss() {}
    override fun draw(canvas: Canvas) {}

    override var sections: Sections? = null
    override var recyclerView: RecyclerView? = null
}

open class SectionLetterPopup : SectionPopup {
    @DrawableRes
    var backgroudResource: Int = R.drawable.fastscroll_popup_round // TODO
    @DimenRes
    var sectionTextSize: Int = R.dimen.notification_action_icon_size //TODO
    @ColorRes
    val sectionTextColor: Int = R.color.notification_action_color_filter
    @DimenRes
    var padding: Int = R.dimen.notification_right_icon_size //TODO
    var sectionTextTypeFace: Typeface = Typeface.DEFAULT
    var isShowing = false

    protected var x = 0
    protected var y = 0
    protected var sectionName: String = ""
    protected var width: Int = 0
    protected var height: Int = 0
    protected var background: Drawable? = null
    protected var textPaint: Paint = Paint()

    override var sections: Sections? = null
    override var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            init()
        }

    protected open fun init() {
        val resources = recyclerView!!.context.resources
        val theme = recyclerView!!.context.theme

        background = ResourcesCompat.getDrawable(resources, backgroudResource, theme)!!

        with(textPaint) {
            color = ResourcesCompat.getColor(resources, sectionTextColor, theme)
            isAntiAlias = true
            textSize = resources.getDimension(sectionTextSize)
            typeface = sectionTextTypeFace
            textAlign = CENTER
        }

        width = (textPaint.textSize + resources.getDimension(padding)).toInt()
        height = width
    }

    override open fun show(section: SectionInfo, x: Int, y: Int) {
        if (recyclerView == null || sections == null) {
            return
        }

        isShowing = true

        sectionName = section.shortName.toString()

        when (sections?.gravity) {
            Gravity.START, Gravity.LEFT -> {
                this.x = sections!!.width.toInt() + sections!!.width.toInt()
                this.y = y - height / 2
            }
            Gravity.END, Gravity.RIGHT -> {
                this.x = (sections!!.left - width).toInt() - sections!!.width.toInt()
                this.y = y - height / 2
            }
        //TODO top / bottom
        }

        //corrections
        if (this.x < 0) this.x = 0
        else if (this.x > recyclerView!!.width - width) this.y = recyclerView!!.width - width
        if (this.y < 0) this.y = 0
        else if (this.y > recyclerView!!.height - height) this.y = recyclerView!!.height - height
    }

    override fun draw(canvas: Canvas) {
        if (!isShowing) return
        drawBackground(canvas)
        drawText(canvas)
    }

    override fun dismiss() {
        isShowing = false
    }

    protected open fun drawBackground(canvas: Canvas) {
        background?.setBounds(x, y, x + width, y + height)
        background?.draw(canvas)
    }

    protected open fun drawText(canvas: Canvas) {
        val offset = ((textPaint.descent() + textPaint.ascent()) / 2f)
        canvas.drawText(sectionName,
                x + width / 2f,
                y + height / 2f - offset,
                textPaint)
    }
}

class SectionCirclePopup : SectionLetterPopup() {

    @ColorInt
    val backgroundColor = Color.parseColor("#3100ff00") //TODO

    override fun init() {
        backgroudResource = R.drawable.fastscroll_popup_round
        super.init()
        background?.mutate()?.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN)
    }
}