package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.drawables.Drawable
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage

class Canvas (val app: PApplet, val previewClip: Clipping, path: String = "") {
    val list = mutableListOf<Drawable>()
    val gCanvas: PGraphics = app.createGraphics(previewClip.width, previewClip.height)
    val imageClip: Clipping

    init {
        if (path != "") {
            val image: PImage = app.loadImage(path)
            imageClip = Clipping(image.width, image.height)
        } else {
            imageClip = previewClip
        }

    }

    fun add(drawable: Drawable):Int {
        gCanvas.beginDraw()
        drawable.draw(gCanvas, 1f)
        gCanvas.endDraw()

        list.add(drawable)
        return list.lastIndex
    }

    fun draw(g: PGraphics) {
        g.blendMode(PApplet.MULTIPLY)
        g.image(this.gCanvas, previewClip.x.toFloat(), previewClip.y.toFloat())
        g.blendMode(PApplet.BLEND)
    }






}