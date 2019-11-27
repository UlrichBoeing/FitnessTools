package de.ulrich_boeing.framework

import processing.core.PGraphics

val drawRoot = Group(null)

interface Drawable {
    var group: Group?
    fun draw(g: PGraphics)
}