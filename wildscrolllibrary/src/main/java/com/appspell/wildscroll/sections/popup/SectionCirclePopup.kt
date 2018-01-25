package com.appspell.wildscroll.sections.popup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v4.content.res.ResourcesCompat
import appspell.com.wildscroll.R


@SuppressLint("ResourceType")
class SectionCirclePopup(
        context: Context,
        @ColorRes
        sectionTextColorRes: Int = R.color.fastscroll_highlight_text,
        @DimenRes
        sectionTextSizeDimen: Int = R.dimen.fastscroll_popup_section_text_size,
        @ColorRes
        backgroundColorRes: Int = R.color.fastscroll_section_background,
        @DimenRes
        paddingRes: Int = R.dimen.fastscroll_popup_padding)
    : SectionLetterPopup(context = context,
        sectionTextColorRes = sectionTextColorRes,
        sectionTextSizeDimen = sectionTextSizeDimen,
        backgroundResource = R.drawable.fastscroll_popup_circle,
        paddingRes = paddingRes) {

    var backgroundColorRes: Int = backgroundColorRes
        set(value) {
            field = value
            val color = ResourcesCompat.getColor(context.resources, value, context.theme)
            background?.mutate()?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

    var backgroundColor: Int? = null
        set(value) {
            background?.mutate()?.setColorFilter(value!!, PorterDuff.Mode.SRC_IN)
        }

    init {
        this.backgroundColorRes = backgroundColorRes
    }

}