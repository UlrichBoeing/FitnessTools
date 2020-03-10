package de.ulrich_boeing.canvas

import differentSize
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.lang.RuntimeException
import kotlin.math.roundToInt

class SingleCanvas(val parent: SizeableCanvas, val size: Float, image: PImage?) {
    val width = (parent.width * size).roundToInt()
    val height = (parent.height * size).roundToInt()
    val g: PGraphics by lazy { createGraphics(parent.app, width, height) }
    var drawnElements = 0

    init {
        if (parent.hasImage) {
            g.beginDraw()
            g.image(image, 0f, 0f, g.width.toFloat(), g.height.toFloat())
            g.endDraw()
        }
    }

    fun drawGraphics(target: PGraphics) {
        if (parent.hasImage || drawnElements > 0) {
            if (target.differentSize(g))
                throw RuntimeException("Can only draw to a PGraphics with the same size.")

            target.blendMode(parent.blendMode)
            target.image(g, 0f, 0f)
        }
    }

    fun drawNextElement(): Boolean {
        if (drawnElements == parent.list.size) {
            return false
        }
        val drawable = parent.list[drawnElements++]

        g.beginDraw()
        drawable.draw(g, size)
        g.endDraw()

        return true
    }

    fun createGraphics(app: PApplet, width: Int, height: Int): PGraphics {
        println("PGraphics wird erstellt: width = $width, height = $height")
        return app.createGraphics(width, height)
    }
}

