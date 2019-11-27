package de.ulrich_boeing.framework

import processing.core.PGraphics

class Group(override var group: Group? = null) : Drawable {
    private val list = mutableListOf<Drawable>()

    override fun draw(g: PGraphics) {
        for (drawable in list) {
            drawable.draw(g)
        }
    }

    fun add(drawable: Drawable) {
        if (drawable.group != null) {
            throw RuntimeException("Drawable is already assigned to group.")
        }
        list.add(drawable)
        drawable.group = this
    }

    fun isEmpty(): Boolean = (list.size == 0)
}