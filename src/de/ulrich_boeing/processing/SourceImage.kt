package de.ulrich_boeing.processing

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.extensions.getPixel
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import kotlin.math.roundToInt

class SourceImage {
    val image: PImage
    val imageClip: Clipping
    var previewImage: PImage? = null
    val previewClip: Clipping

    constructor(app: PApplet, path: String, previewArea: Clipping) {
        this.image = app.loadImage(path)
        this.imageClip = Clipping(image.width, image.height)
        this.previewClip = imageClip.shrinkInto(previewArea).centerInto(previewArea)
        this.image.loadPixels()
    }

    operator fun get(x: Float, y: Float): Int {
        val normX = x  / previewClip.width
        val normY = y / previewClip.height
        val newX = (normX * imageClip.width).roundToInt()
        val newY = (normY * imageClip.height).roundToInt()
        return image.getPixel(newX, newY)
    }

    // Is expecting preview-coordinates
    fun contains(x: Int, y: Int): Boolean =
        !(x < previewClip.x || y < previewClip.y || x > previewClip.right || y >= previewClip.bottom)


    fun showPreview(g: PGraphics, alpha: Int = 72) {
        if (previewImage == null) {
            previewImage = createPreviewImage()
            println("preview is created")
        }
        g.tint(255f, alpha.toFloat())
        g.image(previewImage!!, previewClip.x.toFloat(), previewClip.y.toFloat())
        g.noTint()
    }

    fun createPreviewImage(): PImage {
        val img = image.copy()
        img.resize(previewClip.width, previewClip.height)
        return img
    }
}