package com.appspell.wildscroll.sections.popup

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import appspell.com.wildscroll.R
import com.appspell.wildscroll.sections.SectionInfo
import com.appspell.wildscroll.sections.Sections


open class SectionLetterPopup(
        @DimenRes
        var sectionTextSize: Int = R.dimen.fastscroll_popup_section_text_size,
        @ColorRes
        val sectionTextColor: Int = R.color.fastscroll_highlight_text,
        @DimenRes
        var padding: Int = R.dimen.fastscroll_popup_padding,
        var sectionTextTypeFace: Typeface = Typeface.DEFAULT,
        @DrawableRes
        var backgroudResource: Int = R.drawable.fastscroll_popup_round
) : SectionPopup {

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

    override fun show(section: SectionInfo, x: Int, y: Int) {
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
