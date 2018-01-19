package com.appspell.wildscroll.sections

import android.support.v4.util.ArrayMap
import android.text.TextUtils
import android.view.Gravity
import com.appspell.wildscroll.adapter.SectionFastScroll
import com.appspell.wildscroll.view.WildScrollRecyclerView
import com.eatigo.common.coroutines.Android
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

interface OnSectionChangedListener {
    fun onSectionChanged()

}

data class SectionInfo(val name: String,
                       val shortName: Char,
                       val position: Int,
                       val count: Int)

class Sections(private val recyclerView: WildScrollRecyclerView) {

    companion object {
        const val UNSELECTED = -1

        private const val SECTION_SHORT_NAME_EMPTY = '-'
        private const val SECTION_SHORT_NAME_DIGITAL = '#'
    }

    var left = 0f
    var top = 0f
    var width = 0f
    var height = 0f
    var gravity = Gravity.RIGHT
    var collapseDigital = true

    var paddingLeft = 200f
    var paddingRight = 20f

    var sections: ArrayMap<Char, SectionInfo> = ArrayMap()

    var selected = UNSELECTED

    private var job: Job? = null

    fun getCount() = sections.size

    fun changeSize(w: Int, h: Int, selectedTextSize: Float) {
        val sectionCount = sections.size
        width = selectedTextSize + paddingLeft + paddingRight
        height = h / sectionCount.toFloat()

        when (gravity) {
            Gravity.START, Gravity.LEFT -> left = 0f
            Gravity.END, Gravity.RIGHT -> left = w - width
        //TODO top / bottom
        }
    }

    fun contains(x: Float, y: Float): Boolean {
        return x >= left &&
                x <= left + width &&
                y >= top &&
                y <= height * sections.size
    }


    fun getSectionInfoByIndex(index: Int): SectionInfo? {
        val key = getSectionByIndex(index)
        return sections[key]
    }

    fun createShortName(name: String): Char =
            when {
                name.isEmpty() -> SECTION_SHORT_NAME_EMPTY
                collapseDigital && TextUtils.isDigitsOnly(name[0].toString()) -> SECTION_SHORT_NAME_DIGITAL
                else -> name[0].toUpperCase()
            }

    fun refresh(listener: OnSectionChangedListener) {
        job?.cancel()
        job = launch(Android) {
            sections = fetchSections().await()
            listener.onSectionChanged()
        }
    }

    private fun fetchSections(): Deferred<ArrayMap<Char, SectionInfo>> {
        return async(CommonPool) {
            val map = ArrayMap<Char, SectionInfo>()

            if (recyclerView.adapter == null) {
                return@async map
            }
            if (recyclerView.adapter.itemCount <= 1 || recyclerView.adapter !is SectionFastScroll) {
                return@async map
            }

            val adapter = recyclerView.adapter as SectionFastScroll

            if (recyclerView.adapter.itemCount > 0) {
                for (position in 0 until recyclerView.adapter.itemCount) {

                    val name = adapter.getSectionName(position)

                    val shortName = createShortName(name)

                    val sectionInfo =
                            if (map.containsKey(shortName)) map[shortName]!!.copy(count = map[shortName]!!.count + 1)
                            else SectionInfo(name, shortName, position, 1)

                    map.put(shortName, sectionInfo)
                }
            }
            return@async map
        }
    }

    private fun getSectionByIndex(index: Int): Char = sections.keyAt(index)
}