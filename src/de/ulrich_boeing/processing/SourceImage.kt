package de.ulrich_boeing.processing

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.extensions.getPixel
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage


class SourceImage(val app: PApplet, val path: String) {
    enum class Type {
        ORIGINAL, PREVIEW
    }

    private var image = Array<PImage?>(Type.values().size) { null }
    private val clipping = Array<Clipping>(Type.values().size) { Clipping() }
    var previewArea: Clipping = Clipping(0, 0, app.width, app.height)
        set(value) {
            field = value
            image[Type.PREVIEW.ordinal] = null
            image(SourceImage.Type.PREVIEW)
        }

    val previewClipping : Clipping
        get() = clipping[Type.PREVIEW.ordinal]


    var curType = Type.ORIGINAL

    /*
        Expecting coordinates in from 0 -> preview.width, 0 -> preview.height
     */
    operator fun get(x: Float, y: Float, type : Type = curType): Int {
        return if (curType == Type.PREVIEW) {
            image(curType).getPixel(x, y)
        } else {
            val xFloat = x * clipping[curType.ordinal].width / clipping[Type.PREVIEW.ordinal].width
            val yFloat = y * clipping[curType.ordinal].height / clipping[Type.PREVIEW.ordinal].height
            image(curType).getPixel(xFloat, yFloat)
        }
    }

    fun image(type: Type): PImage {
        val number = type.ordinal

        if (image[number] != null) {
            return image[number]!!
        }

        lateinit var img: PImage
        when (type) {
            Type.ORIGINAL -> {
                img = app.loadImage(path)
                clipping[number] = Clipping(0, 0, img.width, img.height)
            }
            Type.PREVIEW -> {
                img = image(Type.ORIGINAL).copy()
                clipping[number] = previewArea.center(previewArea.shrinkInto(clipping[Type.ORIGINAL.ordinal]))
                img.resize(clipping[number].width, clipping[number].height)
            }
        }

        image[number] = img
        img.loadPixels()
        return img
    }

//    fun load(path: String): PImage {
//        this.path = path
//        return image(Type.ORIGINAL)
//    }

    fun show(g: PGraphics, alpha : Int = 72) {
        val preview = image(Type.PREVIEW)
        val previewSize = clipping[Type.PREVIEW.ordinal]
        previewSize.draw(g)
        g.tint(255f, alpha.toFloat())
        g.image(preview, previewSize.x.toFloat(), previewSize.y.toFloat())
        g.noTint()
    }

    // Is expecting preview-coordinates
    fun isOutside(x: Float, y: Float): Boolean =
        (x < 0 || y < 0 || x >= image(Type.PREVIEW).width || y >= image(Type.PREVIEW).height)

}