package de.ulrich_boeing.canvas

import differentSize
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.lang.RuntimeException
import org.tinylog.kotlin.Logger
import kotlin.math.roundToInt

internal class SingleCanvas(val parent: SizeableCanvas, val size: Float) {
    val width = (parent.width * size).roundToInt()
    val height = (parent.height * size).roundToInt()
    private val g: PGraphics by lazy { createGraphics(parent.app, width, height, parent.image) }
    var drawnElements = 0

    fun drawGraphics(target: PGraphics) {
        if (parent.hasImage || drawnElements > 0) {
            if (target.differentSize(g))
                throw RuntimeException("Can only draw to a PGraphics with the same size.")

            target.blendMode(parent.blendMode)
            target.image(g, 0f, 0f)
        }
    }

    fun getColor(x: Float, y: Float): Int {
        val xInt = (x * size).toInt()
        val yInt = (y * size).toInt()
        val index = xInt + yInt * g.width
        g.loadPixels()
        val c = g.pixels[index]
        g.updatePixels()
        return c
    }

    fun isRenderingComplete(): Boolean = (drawnElements >= parent.list.size)

    fun renderNextElement(): Boolean {
        if (isRenderingComplete()) {
//            Logger.info("Render is already complete")
            return false
        }
        Logger.info("Draw element $drawnElements in size ${size.toString()}")
        val drawable = parent.list[drawnElements++]

        g.beginDraw()
        drawable.draw(g, size)
        g.endDraw()

        return true
    }

    fun save(path: String) {
        if (drawnElements > 0) {
            println(path)
            g.save(path)
        }
    }

}

fun createGraphics(app: PApplet, width: Int, height: Int, image: PImage?): PGraphics {
    println("PGraphics wird erstellt: width = $width, height = $height")
    val localG = app.createGraphics(width, height)

    if (image != null) {
        localG.beginDraw()
        localG.image(image, 0f, 0f, localG.width.toFloat(), localG.height.toFloat())
        localG.endDraw()
    }
    return localG
}
