package de.ulrich_boeing.elements

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.fitness_tools.Stats
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import de.ulrich_boeing.utility.Timespan
import java.lang.RuntimeException
import kotlin.random.Random

// Verschiedene Ebenen werden gerendert in verschiedenen Größen,
// Ebenen (Elemente) werden gelockt und nur als Hintergrund eingefügt
class Layout(app: PApplet, image: PImage) {
    companion object {
        lateinit var app: PApplet
    }

    init {
        Layout.app = app
    }


    val elements = mutableListOf<Element>()
    val image: PImage = image
    var fullRender: PGraphics? = null
    val stats = Stats(this)

    val backgroundColor = Color.fromRGBA(0, 0, 0, 255)

    var fitness: Double = -1.0
        get() {
            if (field == -1.0)
                throw RuntimeException("Fitness is not calculated yet. Please render() before.")

            return field
        }
        private set


    fun add(e: Element) {
        if (fullRender != null)
            throw RuntimeException("Can't add to Layout which has been rendered.")

        elements.add(e)
    }

//    fun mutate(i: Int): Layout {
//        val newLayout = Layout(app)
//        newLayout.elements.addAll(elements)
//
//
//        newLayout.elements[i] = newLayout.elements[i].mutate()
//        return newLayout
//    }

    fun render(image: PImage) {
        fullRender = app.createGraphics(image.width, image.height)
        if (fullRender != null) {
            draw(fullRender!!)
//            fitness = compareImage(fullRender!!, image)
        }
    }


//    fun getLastFitElement(image: PImage): Int {
//        setFitnessForAll(image)
//        var min = 0
//        for (i in elements.indices) {
//            if (elements[i].fitness <= elements[min].fitness) {
//                min = i
//            }
//        }
//        return min
//    }

//    fun setFitnessForAll(image: PImage) {
//        for (i in elements.indices)
//            elements[i].fitness = elementFitness(image, i)
//    }


    fun drawClippings(clippings: Array<Clipping>): Array<PGraphics> {
        return Array<PGraphics>(clippings.size) { i -> drawClipping(clippings[i]) }
    }

    fun drawClipping(clipping: Clipping): PGraphics {
        val offset = Vec(clipping.x, clipping.y)
        val clip = app.createGraphics(clipping.width, clipping.height)

        clip.beginDraw()
        clip.background(backgroundColor.rgba)
        for (e in elements) {
            if (clipping.isOverlapping(e.clipping)) {
                clip.image(e.image, (e.clipping.x - offset.x), (e.clipping.y - offset.y))
            }
        }
        clip.endDraw()
        return clip
    }

    fun evaluateGraphics(graphics: Array<PGraphics>, clippings: Array<Clipping>): Long {
        var sum = 0L
        for (i in clippings.indices) {
            sum += image.getDifference(graphics[i], clippings[i].x, clippings[i].y)
        }
        return sum
    }

    fun evaluateElement(index: Int, newElement: Element) {
        val element = elements[index]
        val clippings = element.clipping.exactUnion(newElement.clipping)

        val g = fullRender ?: throw RuntimeException("Element evaluation needs fullRender")
        val difference = image.getDifference(g, clippings)

        elements[index] = newElement
        val newGraphics = drawClippings(clippings)
        val newDifference = evaluateGraphics(newGraphics, clippings)

        if (newDifference < difference) {
            elements[index] = newElement
            for (i in newGraphics.indices) {
                g.image(newGraphics[i], clippings[i].x.toFloat(), clippings[i].y.toFloat())
            }
        } else {
            elements[index] = element
        }
    }

    fun mutate(index: Int = Random.nextInt(elements.size)) {
        val newElement = Circle()
        evaluateElement(index, newElement)
    }


//    fun elementFitness(image: PImage, index: Int): Double {
//        timing.start()
//        val g1 = renderElement(index, false)
//        val g2 = renderElement(index, true)
//        stats.drawTime += timing.get()
//
//        val rect = elements[index].clipping
//        val intRect = rect.toIntRect()
//
//        timing.start()
//        val diff1 = image.getDifference(g1, intRect.x, intRect.y)
//        val diff2 = image.getDifference(g2, intRect.x, intRect.y)
//        stats.evaluationTime += timing.get()
//        // positive value means positive effect of element
//        return diff1 - diff2
//    }

//    fun newElementFitness(image: PImage, index: Int, replacement: Element): Double {
//        val oldElement = elements[index]
//        elements[index] = replacement
//
//        timing.start()
//        val g1 = renderElement(index, false)
//        val g2 = renderElement(index, true)
//        stats.drawTime += timing.get()
//
//        val rect = elements[index].clipping
//        val intRect = rect.toIntRect()
//        timing.start()
//        val diff1 = image.getDifference(g1, intRect.x, intRect.y)
//        val diff2 = image.getDifference(g2, intRect.x, intRect.y)
//        stats.evaluationTime += timing.get()
//
//        elements[index] = oldElement
//        // positive value means positive effect of element
//        return diff1 - diff2
//    }

//    fun renderElement(index: Int, include: Boolean): PGraphics {
//        val rect = elements[index].clipping
//        val intRect = rect.toIntRect()
//
//        val offset = Vec(rect.x, rect.y)
//
//        val gElement = app.createGraphics(intRect.width, intRect.height)
//        gElement.beginDraw()
//        gElement.background(0)
////        gElement.blendMode(PApplet.ADD)
//        for (t in elements.indices) {
//            if (include || (t != index))
//                elements[t].drawWithOffset(gElement, offset)
//        }
//        gElement.endDraw()
//        return gElement
//    }

    private fun draw(g: PGraphics) {
        val timing = Timespan().start()
        g.beginDraw()
//        g.blendMode(PApplet.ADD)
        g.background(0)
        for (e in elements) {
            g.image(e.image, e.clipping.x.toFloat(), e.clipping.y.toFloat())
//            e.draw(g)
        }
        g.endDraw()
        stats.drawAllTime += timing.time
        stats.drawAllCount++
    }

    fun compareImage(image: PImage): Double {
        val g = fullRender ?: throw RuntimeException("Element evaluation needs fullRender")
        g.loadPixels()
        image.loadPixels()

        var sum = 0.0
        for (i in image.pixels.indices) {
            val originalPixel = Color(image.pixels[i])
            val pixel = Color(g.pixels[i])
            sum += originalPixel.getDiff(pixel)
        }

        g.updatePixels()
        image.updatePixels()

        // current: the smaller the fitness, the better
        return sum / (image.pixels.size)
    }
}
