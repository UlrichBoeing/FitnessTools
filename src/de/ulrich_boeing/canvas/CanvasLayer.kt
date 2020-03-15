package de.ulrich_boeing.canvas

import de.ulrich_boeing.basics.Timing
import de.ulrich_boeing.basics.Vec
import de.ulrich_boeing.extensions.fileNameFromPath
import de.ulrich_boeing.drawables.Drawable
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import kotlin.math.roundToInt
import kotlin.math.sqrt


class CanvasLayer(
    val app: PApplet,
    val width: Int,
    val height: Int,
    val sizes: Map<CanvasSize, Float> = mapOf(CanvasSize.PREVIEW to 0.8f, CanvasSize.OUTPUT to 2f)

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
//    private var curLayer = SizeableCanvas(this, sizes, null)
//    private var _curLayer: Int = 1
    var curLayer: Int = 1
            get() = field
            set(index) {
                if (!layers.containsKey(index)) {
                    layers[index] = SizeableCanvas(this, sizes, null)
                    layers = layers.toSortedMap()
                }
                field =  index
            }

    private val curSizeableCanvas: SizeableCanvas
        get() = layers.getValue(curLayer)

    private var curReadIndex: Int = 1
    private var lastFrameAdded = -1
    var name: String = ""

    init {
        curLayer = 1
    }

    fun add(drawable: Drawable) {
        curSizeableCanvas.add(drawable)
        lastFrameAdded = app.frameCount
    }

    fun setBlendMode(blendMode: Int) {
        curSizeableCanvas.blendMode = blendMode
    }

    fun getColor(vec: Vec, index: Int = curReadIndex): Int = getColor(vec.x, vec.y, index)
    fun getColor(x: Float, y: Float, index: Int = curReadIndex): Int {
        val readLayer = layers[index]
        if (readLayer != null) {
            return readLayer.canvas[CanvasSize.PREVIEW]!!.getColor(x, y)
        } else {
            throw RuntimeException("Cannot read color value, canvas does not exist.")
        }
    }

    fun previewToStd(vec: Vec): Vec {
        vec.x /= sizes.getValue(CanvasSize.PREVIEW)
        vec.y /= sizes.getValue(CanvasSize.PREVIEW)
        return vec
    }

//    private fun _setCurLayer(index: Int): Int {
//        if (!layers.containsKey(index)) {
//            layers[index] = SizeableCanvas(this, sizes, null)
//            layers = layers.toSortedMap()
//        }
//        return index
//    }

    fun addImageLayer(index: Int, image: PImage): Int {
        if (layers.containsKey(index))
            throw RuntimeException("Cannot add image-layer to already existing index $index")

        layers[index] = SizeableCanvas(this, sizes, image)
        curReadIndex = index
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
                println("layer $index is saved")
            }
        }
    }

    /**
     *
     */
    fun isRenderingComplete(size: CanvasSize): Boolean {
        for ((_, canvas: SizeableCanvas) in layers) {
            if (!canvas.isRenderingComplete(size))
                return false
        }
        return true
    }

    /**
     *
     * (default rate is 60fps, ~17ms for each draw())
     * renderDuration

     */
    fun render() {
        val renderDuration = 100L
        val timing = Timing()
        val sizeEnd = if (appIsIdle()) CanvasSize.OUTPUT else CanvasSize.PREVIEW
//        Logger.info("Current sizeLimit = ${sizeEnd.name}")
        while (timing.get() < renderDuration) {
            if (!renderNextElement(CanvasSize.PREVIEW, sizeEnd)) {
                return
            }
        }
    }

    /**
     * Rendering of one drawable
     * Returns true if there was a drawable to render
     * returns false if every drawable inside the range of sizes is already rendered
     */
    fun renderNextElement(
        sizeStart: CanvasSize = CanvasSize.PREVIEW,
        sizeEnd: CanvasSize = CanvasSize.OUTPUT
    ): Boolean {
        val sizesToRender = CanvasSize.values().filter { it >= sizeStart && it <= sizeEnd }
        for (size in sizesToRender) {
            for ((_, canvas) in layers) {
                if (!canvas.readOnly)
                    if (canvas.renderNextElement(size)) {
                        return true
                    }
            }
        }
        return false
    }

    fun contains(vec: Vec): Boolean = contains(vec.x, vec.y)

    fun contains(x: Float, y: Float): Boolean = !(x < 0 || y < 0 || x > width || y > height)

    fun appIsIdle(): Boolean = !(app.mousePressed || app.keyPressed || lastFrameAdded > app.frameCount - 4)


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