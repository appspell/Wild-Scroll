package com.appspell.wildscroll.sections.popup

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import com.appspell.wildscroll.sections.SectionInfo
import com.appspell.wildscroll.sections.Sections

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