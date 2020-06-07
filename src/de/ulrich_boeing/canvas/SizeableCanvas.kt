package de.ulrich_boeing.canvas

import de.ulrich_boeing.drawables.Drawable
import org.tinylog.Logger
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage

enum class CanvasSize {
    PREVIEW, S, M, L, OUTPUT
}

internal class SizeableCanvas(val parent: CanvasLayer, sizes: Map<CanvasSize, Float>, val image: PImage?) {
    val list = mutableListOf<Drawable>()
    val canvas: MutableMap<CanvasSize, SingleCanvas> = mutableMapOf()
    var blendMode = PApplet.BLEND
    val hasImage = (image != null)
    val readOnly = hasImage
    var visible = true

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
        if (!readOnly) {
            list.add(drawable)
            Logger.info {"Element ${list.lastIndex} added to list."}
        }
    }

    fun drawGraphics(target: PGraphics, size: CanvasSize) {
        val singleCanvas = canvas[size]
        if (singleCanvas != null) {
            singleCanvas.drawGraphics(target)
        }
    }

    fun isRenderingComplete(size: CanvasSize): Boolean = canvas[size]?.isRenderingComplete() ?: true

    fun renderNextElement(size: CanvasSize): Boolean = canvas[size]?.renderNextElement() ?: false
}