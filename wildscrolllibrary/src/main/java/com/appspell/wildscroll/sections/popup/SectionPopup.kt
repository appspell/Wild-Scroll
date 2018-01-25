package com.appspell.wildscroll.sections.popup

import android.graphics.Canvas
import android.graphics.Rect
import com.appspell.wildscroll.sections.Sections

interface SectionPopup {
    fun show(section: String, x: Int, y: Int, parentWidth: Int, parentHeight: Int, sections: Sections)
    fun dismiss()
    fun draw(canvas: Canvas)
    fun getRect(): Rect

    var onDismissListener: (() -> Unit)?
    var width: Int
    var height: Int
}

class StubSectionPopup : SectionPopup {
    override fun show(section: String, x: Int, y: Int, parentWidth: Int, parentHeight: Int, sections: Sections) {}
    override fun dismiss() {}
    override fun draw(canvas: Canvas) {}
    override fun getRect(): Rect = Rect(0, 0, 0, 0)

    override var onDismissListener: (() -> Unit)? = null
    override var width: Int = 0
    override var height: Int = 0
}