package de.ulrich_boeing.canvas

import de.ulrich_boeing.extensions.fileNameFromPath
import de.ulrich_boeing.framework.Drawable
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.io.File
import kotlin.math.roundToInt
import kotlin.math.sqrt


class CanvasLayer(val app: PApplet, val width: Int, val height: Int) {
    companion object {
        fun createFromImage(app: PApplet, path: String): CanvasLayer {
            val standardSize = 1000000f
            val image = app.loadImage(path)
            val width = sqrt(standardSize * image.width / image.height)
            val height = sqrt(standardSize * image.height / image.width)
            val canvasLayer = CanvasLayer(app, width.roundToInt(), height.roundToInt())
            canvasLayer.addLayer(0, image)
            canvasLayer.name = path.fileNameFromPath()
            return canvasLayer
        }
    }
    val layers: MutableMap<Int, SizeableCanvas> = mutableMapOf()
    val sizes: MutableMap<CanvasSize, Float> = mutableMapOf(CanvasSize.S to 0.8f, CanvasSize.OUTPUT to 1f)
    var name: String = ""

    fun add(index: Int, drawable: Drawable) {
        if (layers.containsKey(index)) {
            layers[index]!!.add(drawable)
        }
    }

    fun setBlendMode(blendMode: Int, index: Int) {
        if (layers.containsKey(index)) {
            layers[index]!!.blendMode = blendMode
        }
    }

    fun getColor(x: Float, y: Float): Int {
        return layers[0]!!.canvas[CanvasSize.S]!!.getColor(x, y)
    }

    fun addLayer(index: Int, image: PImage? = null): SizeableCanvas {
        if (layers.containsKey(index))
            throw RuntimeException("Cannot add layer with already existing index $index")

//        if (image != null) {
//            addImageSizeToSizes(image)
//        }
        val newLayer = SizeableCanvas(this, sizes, image)

//        sizes.remove(CanvasSize.IMAGE)

        layers[index] = newLayer
        return newLayer
    }

    fun getImage(size: CanvasSize): PGraphics? {
        val sizeFactor = sizes[size] ?: 0f
        if (sizeFactor <= 0) {
            return null
        } else {
            val imageWidth = (width * sizeFactor).roundToInt()
            val imageHeight = (height * sizeFactor).roundToInt()
            val g = app.createGraphics(imageWidth, imageHeight)
            g.beginDraw()
            for ((index, canvas) in layers) {
                canvas.drawGraphics(g, size)
//                if (canvas.canvas.containsKey(size)) {
//                    g.image(canvas.canvas[size]!!.g, 0f, 0f, imageWidth.toFloat(), imageHeight.toFloat())
//                }
            }
            g.endDraw()
            return g
        }
    }

    fun saveLayers(size: CanvasSize = CanvasSize.OUTPUT) {
        for ((index, layer) in layers) {
            val singleCanvas = layer.canvas[size]
            if (singleCanvas != null) {
                println("save layer $index with size ${singleCanvas.size}")
                singleCanvas.save("E:/Temp/processing/" + name + " " + size.toString() + index.toString() + ".png")
            }
        }
    }

    fun drawNextElement(): Boolean {
        for (size in CanvasSize.values()) {
            for ((index, canvas) in layers) {
                if (canvas.drawNextElement(size)) {
                    println(size.toString())
                    return true
                }
            }
        }
        return false
    }

//    fun addImageSizeToSizes(image: PImage) {
//        sizes[CanvasSize.IMAGE] = getSizeFactorOfImage(image)
//    }

    fun getSizeFactorOfImage(image: PImage): Float {
        val areaOfCanvas = (width * height).toFloat()
        val areaOfImage = (image.width * image.height).toFloat()
        val factor = sqrt(areaOfImage / areaOfCanvas)
        return factor
    }

//    fun getCurrentNumber(): Int {
//        val folder = File(folderName)
//        val files: Array<File> = folder.listFiles()
//        val regex =
//            "^" + baseName + delimiter + numberFormat.toString() + "\\." + extension + "$"
//        val pattern: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
//        var maxNumber = 0
//        for (file in files) {
//            if (file.isFile) {
//                // System.out.println(file.getName());
//                val matcher: Matcher = pattern.matcher(file.name)
//                while (matcher.find()) {
//                    val number: Int = matcher.group(1).toInt()
//                    if (maxNumber < number) maxNumber = number
//                }
//            }
//        }
//        return ++maxNumber
//    }

}