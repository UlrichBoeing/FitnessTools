package de.ulrich_boeing.canvas

import de.ulrich_boeing.framework.Drawable
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage

enum class CanvasSize {
    PREVIEW, S, M, L, OUTPUT
}

class SizeableCanvas(val parent: CanvasLayer, sizes: Map<CanvasSize, Float>, val image: PImage?) {
    val list = mutableListOf<Drawable>()
    var canvas: MutableMap<CanvasSize, SingleCanvas> = mutableMapOf()
    var blendMode = PApplet.BLEND
    val hasImage = (image != null)
    val readOnly = hasImage

    val app: PApplet
        get() = parent.app
    val width: Int
        get() = parent.width
    val height: Int
        get() = parent.height

    init {
        // create different sizes
        for ((canvasSize, size) in sizes) {
            canvas[canvasSize] = SingleCanvas(this, size)
        }
    }

    fun add(drawable: Drawable) {
        if (!readOnly)
            list.add(drawable)
    }

    fun drawGraphics(target: PGraphics, size: CanvasSize) {
        val singleCanvas = canvas[size]
        if (singleCanvas != null) {
            singleCanvas.drawGraphics(target)
        }
    }

    fun drawNextElement(size: CanvasSize): Boolean = canvas[size]?.drawNextElement() ?: false
}