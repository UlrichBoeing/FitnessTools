package de.ulrich_boeing.elements

import de.ulrich_boeing.basics.Color
import de.ulrich_boeing.basics.Rect
import de.ulrich_boeing.basics.Vec
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.lang.RuntimeException
import kotlin.random.Random

class Layout(val app: PApplet) {
    private val elements = mutableListOf<Element>()
    var g: PGraphics? = null

    var fitness: Double = -1.0
        get() {
            if (field == -1.0)
                throw RuntimeException("Fitness is not calculated yet. Please render() before.")

            return field
        }
        private set(value) {
            field = value
        }


    fun add(e: Element) {
        if (g != null)
            throw RuntimeException("Can't add to Layout which has been rendered.")

        elements.add(e)
    }

    fun mutate(): Layout {
        val newLayout = Layout(app)
        newLayout.elements.addAll(elements)

        for (i in 1..1) {
            val i = Random.nextInt(newLayout.elements.size)
            newLayout.elements[i] = newLayout.elements[i].mutate()
        }
        return newLayout
    }

    fun render(image: PImage) {
        g = app.createGraphics(image.width, image.height)
        if (g != null) {
            draw(g!!)
            fitness = compareImage(g!!, image)
        }
    }

    fun renderElement(i: Int): PGraphics {
        val rect = elements[i].rect.toIntRect()
        val offset = Vec(rect.x, rect.y)

        val gElement = app.createGraphics(rect.width, rect.height)
        gElement.beginDraw()
        gElement.background(0)
        for (e in elements) {
            e.drawWithOffset(gElement, offset)
        }
        gElement.endDraw()
        return gElement
    }

    private fun draw(g: PGraphics) {
        g.beginDraw()
        g.background(0)
        for (e in elements) {
            e.draw(g)
        }
        g.endDraw()
    }

    private fun compareImage(g: PGraphics, image: PImage): Double {
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
