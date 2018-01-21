package com.appspell.wildscroll.sections.popup

import android.graphics.Canvas
import android.graphics.Rect
import com.appspell.wildscroll.sections.SectionInfo
import com.appspell.wildscroll.sections.Sections
import com.appspell.wildscroll.view.WildScrollRecyclerView

interface SectionPopup {
    fun show(section: SectionInfo, x: Int, y: Int)
    fun dismiss()
    fun draw(canvas: Canvas)
    var recyclerView: WildScrollRecyclerView?
    var sections: Sections?
    fun getRect(): Rect
}

class StubSectionPopup : SectionPopup {
    override fun show(section: SectionInfo, x: Int, y: Int) {}
    override fun dismiss() {}
    override fun draw(canvas: Canvas) {}
    override fun getRect(): Rect = Rect(0, 0, 0, 0)

    override var sections: Sections? = null
    override var recyclerView: WildScrollRecyclerView? = null
}