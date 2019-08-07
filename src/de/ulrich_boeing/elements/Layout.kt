package de.ulrich_boeing.elements

import de.ulrich_boeing.basics.Color
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import kotlin.random.Random

class Layout(val app: PApplet) {
    var g : PGraphics = app.createGraphics(app.width, app.height)
    val list = mutableListOf<Element>()

    fun add(e: Element) {
        list.add(e)
    }

    fun mutate(): Layout {
        val newLayout = Layout(app)
        newLayout.list.addAll(list)

        for (i in 1..1) {
            val i = Random.nextInt(newLayout.list.size)
            newLayout.list[i] = newLayout.list[i].mutate()
        }
        return newLayout
    }

    fun draw() {
        g.beginDraw()
        g.background(255)
        for (e in list) {
            e.draw(g)
        }
        g.endDraw()
    }

    fun fitness(image: PImage): Double {
        draw()
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
        return sum / (image.width * image.height)
    }
}