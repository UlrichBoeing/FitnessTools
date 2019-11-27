package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.Clipping
import processing.core.PApplet
import processing.core.PGraphics

class Canvas(val app: PApplet, val preview: Clipping) : Drawable {
    override var group: Group? = null
    val countLayers = 10
    val layer = Array<Group>(10) {Group(null)}
    val previewLayer = Array<PGraphics?>(countLayers) {null}

    fun add(drawable: Drawable, numLayer: Int = 1) {
        val curLayer = layer[numLayer]
        curLayer.add(drawable)

        val g = previewLayer[numLayer] ?: app.createGraphics(preview.width, preview.height)
        previewLayer[numLayer] = g

        g.beginDraw()
        drawable.draw(g)
        g.endDraw()
    }

    override fun draw(g: PGraphics) {
        for ( i in previewLayer.indices) {
            if (previewLayer[i] != null) {
                g.image(previewLayer[i], preview.x.toFloat(), preview.y.toFloat())
            }
        }
    }

}