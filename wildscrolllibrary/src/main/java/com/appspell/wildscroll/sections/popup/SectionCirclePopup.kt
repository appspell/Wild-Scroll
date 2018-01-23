package com.appspell.wildscroll.sections.popup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import appspell.com.wildscroll.R


@SuppressLint("ResourceType")
class SectionCirclePopup(
        context: Context,
        @DimenRes
        sectionTextSize: Int = R.dimen.fastscroll_popup_section_text_size,
        @ColorRes
        sectionTextColor: Int = R.color.fastscroll_highlight_text,
        @ColorInt
        val backgroundColor: Int = R.color.fastscroll_section_background)
    : SectionLetterPopup(context = context,
        sectionTextSize = sectionTextSize,
        sectionTextColor = sectionTextColor,
        backgroudResource = R.drawable.fastscroll_popup_round) {

    init {
        background?.mutate()?.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN)
    }
}