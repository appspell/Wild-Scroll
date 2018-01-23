package com.appspell.wildscroll.sections.popup

import android.graphics.Canvas
import android.graphics.Rect

interface SectionPopup {
    fun show(section: String, x: Int, y: Int)
    fun dismiss()
    fun draw(canvas: Canvas)
    fun getRect(): Rect

    var onDismissListener: (() -> Unit)?
    val width: Int
    val height: Int
}

class StubSectionPopup : SectionPopup {
    override fun show(section: String, x: Int, y: Int) {}
    override fun dismiss() {}
    override fun draw(canvas: Canvas) {}
    override fun getRect(): Rect = Rect(0, 0, 0, 0)

    override var onDismissListener: (() -> Unit)? = null
    override val width: Int = 0
    override val height: Int = 0
}