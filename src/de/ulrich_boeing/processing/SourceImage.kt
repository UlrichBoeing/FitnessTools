package de.ulrich_boeing.processing

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.basics.Rect
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.util.*
import kotlin.math.roundToInt
enum class Images {
    ORIGINAL, PREVIEW
}

class SourceImage(val app: PApplet) {
    val countImages = 2
    val images = Array<PImage?>(countImages) { null }
    val size = Array<Clipping>(countImages) { Clipping() }

    lateinit var image: PImage
    lateinit var preview : PImage

    var previewSize=  Clipping()
    var imageSize = Clipping()

    var path = ""
    var curIndex = -1

    val ORIGINAL = 0
    val PREVIEW = 1


    inline var pixel: Int
        get() = image.pixels[curIndex]
        set(value) {
            image.pixels[curIndex] = value
        }

//    inline operator fun get(x: Int, y: Int): Int =
//        preview.pixels[x + y * preview.width]
//

    fun load(path: String) {
        this.path = path
        image = app.loadImage(path)

        val img =  app.loadImage(path)
        images[ORIGINAL] = img

        imageSize = Clipping(0, 0, img.width, img.height)
        image.loadPixels()
        img.loadPixels()
    }

    fun show(g: PGraphics, previewArea: Clipping) {
        if (previewSize.size == 0) {
            previewSize = previewArea.shrinkInto(imageSize)
            previewSize = previewArea.center(previewSize)
            preview = image.copy()
            preview.resize(previewSize.width, previewSize.height)
            preview.resize(previewSize.width / 20, previewSize.height / 20)
            preview.resize(previewSize.width, previewSize.height)
        }
        g.image(preview, previewSize.x.toFloat(), previewSize.y.toFloat())
    }

    // Is expecting image-coordinates
    private fun getIndex(x: Int, y: Int): Int {
        if (x < 0 || y < 0 || x >= image.width || y >= image.height) {
            curIndex = -1
            return curIndex
        }
        curIndex = x + y * image.width
        return curIndex
    }

    fun getIndex(x: Float, y: Float): Int {
        return getIndex(x.roundToInt(), y.roundToInt())
    }
}