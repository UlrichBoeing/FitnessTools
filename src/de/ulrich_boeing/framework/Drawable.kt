package de.ulrich_boeing.framework

import processing.core.PGraphics

interface Drawable {
    fun draw(g: PGraphics, size: Float)
}