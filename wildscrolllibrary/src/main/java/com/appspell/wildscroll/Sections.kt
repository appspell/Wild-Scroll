package com.appspell.wildscroll

import android.view.Gravity

class Sections {

    companion object {
        const val UNSELECTED = -1;
    }

    var offsetX = 0f
    var offsetY = 0f
    var width = 0f
    var height = 0f
    var gravity = Gravity.RIGHT

    val sections: List<String> by lazy {
        val list = ArrayList<String>()
        "#abcdefGHIJKLmnopqrSTUVWXyz".forEach { list.add(it.toString()) }
        return@lazy list
    }

    var section = UNSELECTED

    fun changeSize(w : Int, h : Int, selectedTextSize : Float) {
        val sectionCount = sections.size
        width = selectedTextSize
        height = h / sectionCount.toFloat()

        when (gravity) {
            Gravity.LEFT -> offsetX = 0f
            Gravity.START -> offsetX = 0f
            Gravity.RIGHT -> offsetX = w - width
            Gravity.END -> offsetX = w - width
        }

        offsetY = height
    }
}