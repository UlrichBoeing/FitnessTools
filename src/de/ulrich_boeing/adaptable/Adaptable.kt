package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.basics.Color
import de.ulrich_boeing.basics.Vec
import de.ulrich_boeing.basics.getDifference
import de.ulrich_boeing.fitness_tools.FloatRange
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

typealias DNA = FloatArray




class Adaptable(val app: PApplet, aDNA: Array<Float>) {
    constructor (app: PApplet, pointsCount: Int = 6): this(app, Array(pointsCount + 1) { Random.nextFloat() })

    val pointsCount = aDNA.size - 1
    val radiusRange = FloatRange(0.25f, 0.95f)
    val color = Color.fromRGBA(0, 0, 255, 180)
    val DNA = aDNA

    fun mutate(rate: Float) {
        for (i in DNA.indices) {
            if (Random.nextFloat() < rate)
                DNA[i] = Random.nextFloat()
        }
    }

    fun createPoints(clipping: Clipping): Array<Vec> {
        val range = Vec.TAU / pointsCount
        val start = range * (DNA.last() - 0.5f)
        val angles = Array(pointsCount) { i ->
            start + i * range
        }

        val r1 = clipping.width / 2f
        val r2 = clipping.height / 2f

        val lengths = Array<Float>(pointsCount) { i -> radiusRange.expand(DNA[i]) }
        return Array(pointsCount) { i ->
            Vec(r1 * lengths[i] * cos(angles[i]), r2 * lengths[i] * sin(angles[i]))
        }
    }



    fun getImage(clipping: Clipping): PGraphics {
        val points = createPoints(clipping)

        val g = app.createGraphics(clipping.width, clipping.height)
        g.beginDraw()
        g.background(0)
        g.noStroke()

        g.translate(clipping.width / 2f, clipping.height / 2f)
        g.fill(color.rgba)
        g.beginShape()
//        curveVertex(center.x + vecs.last().x, center.y + vecs.last().y)
        for (i in 1..2)
            for (point in points) {
                g.curveVertex(point.x, point.y)
            }
//        curveVertex(center.x + vecs.first().x, center.y + vecs.first().y)
//        curveVertex(center.x + vecs[2].x, center.y + vecs[2].y)
        g.endShape()
        g.endDraw()
        return g
    }

    fun fitness(image: PImage, graphic: PGraphics): Float {
        val dif = image.getDifference(graphic)
        val colorValuesCount = graphic.width * graphic.height * 3 * 255
        return 1 - (dif / colorValuesCount.toFloat())
    }
}