package de.ulrich_boeing.canvas

import de.ulrich_boeing.extensions.fileNameFromPath
import de.ulrich_boeing.framework.Drawable
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.io.File
import kotlin.math.roundToInt
import kotlin.math.sqrt


class CanvasLayer(
    val app: PApplet,
    val width: Int,
    val height: Int,
    val sizes : Map<CanvasSize, Float> = mapOf(CanvasSize.PREVIEW to 0.8f, CanvasSize.OUTPUT to 8f)
) {
    companion object {
        fun createFromImage(app: PApplet, path: String): CanvasLayer {
            val standardSize = 1000000f
            val image = app.loadImage(path)
            val width = sqrt(standardSize * image.width / image.height)
            val height = sqrt(standardSize * image.height / image.width)
            val canvasLayer = CanvasLayer(app, width.roundToInt(), height.roundToInt())
            canvasLayer.addImageLayer(0, image)
            canvasLayer.name = path.fileNameFromPath()
            return canvasLayer
        }
    }

    private var layers: MutableMap<Int, SizeableCanvas> = mutableMapOf()
    private var curLayer : SizeableCanvas
    private var curReadLayer : SizeableCanvas
    var name: String = ""
    init {
        curLayer = SizeableCanvas(this, sizes, null)
        layers[1] = curLayer
        curReadLayer = curLayer
    }

    fun add(drawable: Drawable) {
        curLayer.add(drawable)
    }

    fun setBlendMode(blendMode: Int) {
        curLayer.blendMode = blendMode
    }

    fun getColor(x: Float, y: Float): Int {
        return curReadLayer.canvas[CanvasSize.PREVIEW]!!.getColor(x, y)
    }


    fun setCurLayer(index: Int) {
        val layer = layers[index]
        if (layer != null) {
            curLayer = layer
        } else {
            curLayer = SizeableCanvas(this, sizes, null)
            layers[index] = curLayer
            layers = layers.toSortedMap()
        }
    }

    fun addImageLayer(index: Int, image: PImage): Int {
        if (layers.containsKey(index))
            throw RuntimeException("Cannot add image-layer to already existing index $index")

        curReadLayer = SizeableCanvas(this, sizes, image)
        layers[index] = curReadLayer
        layers = layers.toSortedMap()

        return index
    }

    fun getImage(size: CanvasSize = CanvasSize.PREVIEW): PGraphics? {
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

//    fun getSizeFactorOfImage(image: PImage): Float {
//        val areaOfCanvas = (width * height).toFloat()
//        val areaOfImage = (image.width * image.height).toFloat()
//        val factor = sqrt(areaOfImage / areaOfCanvas)
//        return factor
//    }

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