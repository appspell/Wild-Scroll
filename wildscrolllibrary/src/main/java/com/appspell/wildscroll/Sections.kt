package com.appspell.wildscroll

import android.view.Gravity

class Sections {

    companion object {
        const val UNSELECTED = -1;
    }

    var left = 0f
    var top = 0f
    var width = 0f
    var height = 0f
    var gravity = Gravity.RIGHT

    var paddingLeft = 200f
    var paddingRight = 20f

    val sections: List<String> by lazy {
        val list = ArrayList<String>()
        "#ABCDEFGHIJKLMNOPQRSTUVWXYZ".forEach { list.add(it.toString()) }
        return@lazy list
    }

    var selected = UNSELECTED

    fun changeSize(w: Int, h: Int, selectedTextSize: Float) {
        val sectionCount = sections.size
        width = selectedTextSize + paddingLeft + paddingRight
        height = h / sectionCount.toFloat()

        when (gravity) {
            Gravity.LEFT -> left = 0f
            Gravity.START -> left = 0f
            Gravity.RIGHT -> left = w - width
            Gravity.END -> left = w - width
        }

        top = height
    }

    fun contains(x: Float, y: Float): Boolean {
        return x >= left &&
                x <= left + width &&
                y >= top &&
                y <= height * sections.size
    }
}