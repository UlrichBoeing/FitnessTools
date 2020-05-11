package de.ulrich_boeing.framework

import de.ulrich_boeing.drawables.Drawable
import processing.core.PGraphics

class Group {
    private val list = mutableListOf<Drawable>()

    fun draw(g: PGraphics, size: Float) {
        for (drawable in list) {
            drawable.draw(g, size)
        }
    }

    fun add(drawable: Drawable) {
//        list.add(drawable)
//        drawable.group = this
    }

    fun isEmpty(): Boolean = (list.size == 0)
}