package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.Vec
import de.ulrich_boeing.basics.setAlpha
import de.ulrich_boeing.framework.*
import processing.core.PGraphics
import kotlin.math.pow
import kotlin.math.roundToInt

class ComplexPolygon(data: DrawableData): Drawable(data) {
    val points: List<Vec>
    init {
        val startRadius = data.size * (data.variance / 100f)
        val countTweening = 0 + (data.complexity * 10 / 100).roundToInt()

        val radius = List(countTweening + 1) { i -> startRadius * 0.66f.pow(i.toFloat()) }

        var initPoints = createPolygon(4).resize(data.size).shiftInCircle(radius[0])
        for (i in 1..radius.lastIndex) {
            val tweenPoints = initPoints.tweenPointsOfShape().shiftInCircle(radius[i])
            initPoints = initPoints.zipTweenPoints(tweenPoints)
        }
        points = initPoints
    }

    override fun draw(g: PGraphics, size: Float) {
        g.fill(data.color.setAlpha(data.maxOpacity))
        g.noStroke()
        g.scale(size)
        points.move(data.position).drawAsShape(g)
    }
}