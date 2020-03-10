package de.ulrich_boeing.framework

import processing.core.PGraphics

class Group() : Drawable {
    private val list = mutableListOf<Drawable>()

    override fun draw(g: PGraphics, size: Float) {
        for (drawable in list) {
            drawable.draw(g, size)
        }
    }

    fun add(drawable: Drawable) {
//        if (drawable.group != null) {
//            throw RuntimeException("Drawable is already assigned to group.")
//        }
//        list.add(drawable)
//        drawable.group = this
    }

    fun isEmpty(): Boolean = (list.size == 0)
}