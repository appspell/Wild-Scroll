package com.appspell.wildscroll.sections

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import appspell.com.wildscroll.R

interface SectionPopup {
    fun show(section: SectionInfo, x: Int, y: Int)
    fun dismiss()
    fun draw(canvas: Canvas)
    var sections: Sections
}

class StubSectionPopup : SectionPopup {
    override fun show(section: SectionInfo, x: Int, y: Int) {}
    override fun dismiss() {}
    override fun draw(canvas: Canvas) {}
    override lateinit var sections: Sections
}

open class SectionLetterPopupImpl(private val recyclerView: RecyclerView) : SectionPopup {

    @DrawableRes
    var backgroudResource: Int = R.drawable.fastscroll_popup_background // TODO
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
    protected var width: Int
    protected var height: Int
    protected var background: Drawable
    protected var textPaint: Paint
    override lateinit var sections: Sections

    init {
        val resources = recyclerView.context.resources

        background = resources.getDrawable(backgroudResource) //TODO
        textPaint = Paint()
        with(textPaint) {
            color = resources.getColor(sectionTextColor)
            isAntiAlias = true
            textSize = resources.getDimension(sectionTextSize)
            typeface = sectionTextTypeFace
            textAlign = CENTER
        }

        width = (textPaint.textSize + resources.getDimension(padding)).toInt()
        height = width
    }

    override fun show(section: SectionInfo, x: Int, y: Int) {
        isShowing = true

        sectionName = section.shortName.toString()

        when (sections.gravity) {
            Gravity.START, Gravity.LEFT -> {
                this.x = sections.width.toInt() + sections.width.toInt()
                this.y = y - height / 2
            }
            Gravity.END, Gravity.RIGHT -> {
                this.x = (sections.left - width).toInt() - sections.width.toInt()
                this.y = y - height / 2
            }
        //TODO top / bottom
        }

        //corrections
        if (this.x < 0) this.x = 0
        else if (this.x > recyclerView.width - width) this.y = recyclerView.width - width
        if (this.y < 0) this.y = 0
        else if (this.y > recyclerView.height - height) this.y = recyclerView.height - height
    }

    override fun draw(canvas: Canvas) {
        if (!isShowing) return
        background.setBounds(x, y, x + width, y + height)
        background.draw(canvas)

        val offset = ((textPaint.descent() + textPaint.ascent()) / 2f)
        canvas.drawText(sectionName,
                x + width / 2f,
                y + height / 2f - offset,
                textPaint)

    }

    override fun dismiss() {
        isShowing = false
    }
}