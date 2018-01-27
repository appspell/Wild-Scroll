package com.appspell.wildscroll.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appspell.wildscroll.R
import com.appspell.wildscroll.R.layout
import com.appspell.wildscroll.adapter.SampleSecondAdapter
import com.appspell.wildscroll.data.DataSource
import com.appspell.wildscroll.sections.Gravity.LEFT
import com.appspell.wildscroll.sections.popup.SectionLetterPopup
import com.appspell.wildscroll.view.WildScrollRecyclerView

class CustomProgrammaticallyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layout.fragment_custom_programmatically, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<WildScrollRecyclerView>(R.id.list)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = SampleSecondAdapter()
            (adapter as SampleSecondAdapter).items = DataSource(resources).companies

            textColor = R.color.colorSecondaryDark
            textSize = R.dimen.textRegular
            textTypeFace = Typeface.SANS_SERIF

            highlightColor = R.color.colorSecondary
            highlightTextSize = R.dimen.fastscroll_section_highlight_text_size
            highlightTextFace = Typeface.DEFAULT_BOLD

            sectionBarPaddingLeft = R.dimen.padding
            sectionBarPaddingRight = R.dimen.padding
            sectionBarCollapseDigital = false
            sectionBarGravity = LEFT

            sectionPopup = SectionLetterPopup(context,
                    textColorRes = R.color.colorSecondaryLight,
                    textSizeDimen = R.dimen.textPopup,
                    textTypeFace = Typeface.DEFAULT_BOLD,
                    backgroundResource = R.drawable.background_popup,
                    paddingRes = R.dimen.padding)
        }
    }
}