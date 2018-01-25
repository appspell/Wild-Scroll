package com.appspell.wildscroll.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import appspell.com.wildscroll.R
import com.appspell.wildscroll.sections.Gravity
import com.appspell.wildscroll.sections.SectionBarView
import com.appspell.wildscroll.sections.popup.SectionCirclePopup
import com.appspell.wildscroll.sections.popup.SectionLetterPopup
import com.appspell.wildscroll.sections.popup.SectionPopup

class WildScrollRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private val sectionBar = SectionBarView(this)

    //Section regular text
    @ColorRes
    var textColor: Int = R.color.fastscroll_default_text
        set(value) {
            sectionBar.textColor = ResourcesCompat.getColor(resources, value, context.theme)
        }
    @DimenRes
    var textSize: Int = R.dimen.fastscroll_section_text_size
        set(value) {
            sectionBar.textSize = resources.getDimension(value)
        }
    var textTypeFace: Typeface = Typeface.DEFAULT
        set(value) {
            sectionBar.textTypeFace = value
        }

    //Section highlighted text
    @ColorRes
    var highlightColor: Int = R.color.fastscroll_highlight_text
        set(value) {
            sectionBar.highlightColor = ResourcesCompat.getColor(resources, value, context.theme)
        }
    @DimenRes
    var highlightTextSize: Int = R.dimen.fastscroll_section_highlight_text_size
        set(value) {
            sectionBar.highlightTextSize = resources.getDimension(value)
        }
    var highlightTextFace: Typeface = Typeface.DEFAULT
        set(value) {
            sectionBar.highlightTextFace = value
        }

    //Section bar settings
    @ColorRes
    var sectionBarBackgroundColor: Int = R.color.fastscroll_section_background
        set(value) {
            sectionBar.sectionBarBackgroundColor = ResourcesCompat.getColor(resources, value, context.theme)
        }
    @DimenRes
    var sectionBarPaddingLeft: Int = R.dimen.fastscroll_section_padding
        set(value) {
            sectionBar.sectionBarPaddingLeft = resources.getDimension(value)
        }
    @DimenRes
    var sectionBarPaddingRight: Int = R.dimen.fastscroll_section_padding
        set(value) {
            sectionBar.sectionBarPaddingRight = resources.getDimension(value)
        }
    var sectionBarCollapseDigital: Boolean = true
        set(value) {
            sectionBar.sectionBarCollapseDigital = value
        }
    var sectionBarGravity: Gravity = Gravity.RIGHT
        set(value) {
            sectionBar.sectionBarGravity = value
        }
    var sectionBarEnable: Boolean = true
        set(value) {
            sectionBar.sectionBarEnable = value
        }

    //Popup section
    var popupEnable: Boolean = true
        set(value) {
            sectionBar.popupEnable = value
        }
    @DrawableRes
    var popupBackgroundDrawable: Int = R.drawable.fastscroll_popup_round
        set(value) {
            if (sectionBar.sectionPopup !is SectionLetterPopup) {
                sectionBar.sectionPopup = SectionLetterPopup(context, backgroundResource = value)
            } else {
                (sectionBar.sectionPopup as SectionLetterPopup).backgroundResource = value
            }
        }
    @ColorRes
    var popupBackgroundColor: Int = R.color.fastscroll_section_background
        set(value) {
            if (sectionBar.sectionPopup is SectionCirclePopup) {
                (sectionBar.sectionPopup as SectionCirclePopup).backgroundColorRes = value
            } else {
                sectionBar.sectionPopup = SectionCirclePopup(context, backgroundColorRes = value)
            }
        }
    @ColorRes
    var popupTextColor: Int = R.color.fastscroll_highlight_text
        set(value) {
            if (sectionBar.sectionPopup is SectionCirclePopup) {
                (sectionBar.sectionPopup as SectionCirclePopup).sectionTextColorRes = value
            } else {
                sectionBar.sectionPopup = SectionCirclePopup(context, sectionTextColorRes = value)
            }
        }
    @DimenRes
    var popupTextSize: Int = R.dimen.fastscroll_popup_section_text_size
        set(value) {
            if (sectionBar.sectionPopup is SectionLetterPopup) {
                (sectionBar.sectionPopup as SectionLetterPopup).sectionTextSizeDimen = value
            } else {
                sectionBar.sectionPopup = SectionCirclePopup(context, sectionTextSizeDimen = value)
            }
        }
    @DimenRes
    var popupPadding: Int = R.dimen.fastscroll_popup_padding
        set(value) {
            if (sectionBar.sectionPopup is SectionLetterPopup) {
                (sectionBar.sectionPopup as SectionLetterPopup).paddingRes = value
            } else {
                sectionBar.sectionPopup = SectionCirclePopup(context, paddingRes = value)
            }
        }
    var sectionPopup: SectionPopup = SectionCirclePopup(context)
        set(value) {
            sectionBar.sectionPopup = value
        }

    init {
        textTypeFace = Typeface.DEFAULT
        highlightTextFace = Typeface.DEFAULT

        sectionPopup = SectionCirclePopup(context, sectionTextColorRes = popupTextColor, sectionTextSizeDimen = popupTextSize, backgroundColorRes = popupBackgroundColor)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.WildScroll, defStyle, 0)
            try {
                // Section Bar Text
                sectionBar.textColor = typedArray.getColor(R.styleable.WildScroll_wildScroll_textColor, ResourcesCompat.getColor(resources, R.color.fastscroll_default_text, context.theme))
                sectionBar.textSize = typedArray.getDimension(R.styleable.WildScroll_wildScroll_textSize, resources.getDimension(R.dimen.fastscroll_section_text_size))
                sectionBar.highlightColor = typedArray.getColor(R.styleable.WildScroll_wildScroll_highlightColor, ResourcesCompat.getColor(resources, R.color.fastscroll_highlight_text, context.theme))
                sectionBar.highlightTextSize = typedArray.getDimension(R.styleable.WildScroll_wildScroll_highlightTextSize, resources.getDimension(R.dimen.fastscroll_section_highlight_text_size))

                // Section Bar main settings
                sectionBar.sectionBarBackgroundColor = typedArray.getColor(R.styleable.WildScroll_wildScroll_sectionBarBackgroundColor, ResourcesCompat.getColor(resources, R.color.fastscroll_section_background, context.theme))
                sectionBar.sectionBarPaddingLeft = typedArray.getDimension(R.styleable.WildScroll_wildScroll_sectionBarPaddingLeft, resources.getDimension(R.dimen.fastscroll_section_padding))
                sectionBar.sectionBarPaddingRight = typedArray.getDimension(R.styleable.WildScroll_wildScroll_sectionBarPaddingRight, resources.getDimension(R.dimen.fastscroll_section_padding))
                sectionBarCollapseDigital = typedArray.getBoolean(R.styleable.WildScroll_wildScroll_sectionBarCollapseDigital, true)
                sectionBarGravity = Gravity.values()[typedArray.getInt(R.styleable.WildScroll_wildScroll_sectionBarGravity, Gravity.RIGHT.ordinal)]
                sectionBarEnable = typedArray.getBoolean(R.styleable.WildScroll_wildScroll_sectionBarEnable, true)

                // Section popup
                if (typedArray.hasValue(R.styleable.WildScroll_wildScroll_popupEnable)) {
                    popupEnable = typedArray.getBoolean(R.styleable.WildScroll_wildScroll_popupEnable, true)
                }
                if (typedArray.hasValue(R.styleable.WildScroll_wildScroll_popupPadding)) {
                    if (sectionBar.sectionPopup is SectionLetterPopup) {
                        (sectionBar.sectionPopup as SectionLetterPopup).padding = typedArray.getDimension(R.styleable.WildScroll_wildScroll_popupPadding, resources.getDimension(R.dimen.fastscroll_popup_padding))
                    }
                }
                if (typedArray.hasValue(R.styleable.WildScroll_wildScroll_popupBackgroundDrawable)) {
                    if (sectionBar.sectionPopup is SectionLetterPopup) {
                        (sectionBar.sectionPopup as SectionLetterPopup).backgroundResource = typedArray.getInt(R.styleable.WildScroll_wildScroll_popupBackgroundDrawable, R.drawable.fastscroll_popup_round)
                    }
                }
                if (typedArray.hasValue(R.styleable.WildScroll_wildScroll_popupBackgroundColor)) {
                    if (sectionBar.sectionPopup is SectionCirclePopup) {
                        (sectionBar.sectionPopup as SectionCirclePopup).backgroundColor = typedArray.getColor(R.styleable.WildScroll_wildScroll_popupBackgroundColor, ResourcesCompat.getColor(resources, R.color.fastscroll_popup_background, context.theme))
                    }
                }
                if (typedArray.hasValue(R.styleable.WildScroll_wildScroll_popupTextColor)) {
                    if (sectionBar.sectionPopup is SectionLetterPopup) {
                        (sectionBar.sectionPopup as SectionLetterPopup).sectionTextColor = typedArray.getColor(R.styleable.WildScroll_wildScroll_popupTextColor, ResourcesCompat.getColor(resources, R.color.fastscroll_highlight_text, context.theme))
                    }
                }
                if (typedArray.hasValue(R.styleable.WildScroll_wildScroll_popupTextSize)) {
                    if (sectionBar.sectionPopup is SectionLetterPopup) {
                        (sectionBar.sectionPopup as SectionLetterPopup).sectionTextSize = typedArray.getDimension(R.styleable.WildScroll_wildScroll_popupTextSize, resources.getDimension(R.dimen.fastscroll_popup_section_text_size))
                    }
                }
            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        sectionBar.onSizeChanged(width, height)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        sectionBar.draw(canvas!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (sectionBar.onTouchEvent(ev)) {
            return true
        }
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
            if (sectionBar.onInterceptTouchEvent(ev)) true
            else super.onInterceptTouchEvent(ev)

    override fun setAdapter(adapter: Adapter<*>?) {
        adapter?.registerAdapterDataObserver(dataObserver)
        super.setAdapter(adapter)
    }

    fun release() {
        adapter?.unregisterAdapterDataObserver(dataObserver)
    }

    private val dataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            sectionBar.invalidateLayout(adapter)
            super.onChanged()
        }
    }
}
