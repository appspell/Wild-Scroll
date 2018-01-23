package com.appspell.wildscroll.sections.popup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import appspell.com.wildscroll.R


open class SectionLetterPopup(
        protected var context: Context,
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
    protected var background: Drawable? = null
    protected var textPaint: Paint = Paint()

    override val width: Int
    override val height: Int

    override var onDismissListener: (() -> Unit)? = null

    init {
        val resources = context.resources
        val theme = context.theme

        background = ResourcesCompat.getDrawable(resources, backgroudResource, theme)!!

        textPaint.run {
            color = ResourcesCompat.getColor(resources, sectionTextColor, theme)
            isAntiAlias = true
            textSize = resources.getDimension(sectionTextSize)
            typeface = sectionTextTypeFace
            textAlign = CENTER
        }

        width = (textPaint.textSize + resources.getDimension(padding)).toInt()
        height = width
    }

    override fun show(section: String, x: Int, y: Int) {
        sectionName = section
        this.x = x
        this.y = y

        isShowing = true
    }

    override fun draw(canvas: Canvas) {
        if (!isShowing) return
        drawBackground(canvas)
        drawText(canvas)
    }

    override fun dismiss() {
        isShowing = false
        onDismissListener?.invoke()
    }

    override fun getRect() = Rect(x, y, x + width, y + height)

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
