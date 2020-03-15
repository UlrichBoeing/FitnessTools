package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.COLOR_BLACK
import de.ulrich_boeing.basics.Vec
import processing.core.PGraphics

data class DrawableData(
    val position: Vec,
    val color: Int = COLOR_BLACK,
    val targetColor: Int = COLOR_BLACK,
    val size: Float = 20f,
    val complexity: Float = 50f,
    val variance: Float = 50f,
    val minOpacity: Int = 10,
    val maxOpacity: Int = 60,
    val className: String = ""

)

abstract class Drawable(val data: DrawableData) {
    abstract fun draw(g: PGraphics, size: Float)
}