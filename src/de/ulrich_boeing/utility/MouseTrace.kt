package de.ulrich_boeing.utility

import de.ulrich_boeing.basics.Vec
import de.ulrich_boeing.basics.square
import processing.core.PApplet
import kotlin.random.Random

class MouseTrace(val app: PApplet, minDistance: Int = 2) {
    var onMousePressed = true

    private val list = mutableListOf<Vec>()
    private var numSpread = 0
    private var maxSpreadDistance = 5f
    private val minDistanceSquared = minDistance.square()

    fun setSpread(num: Int, maxDistance: Float = num * 2f): MouseTrace {
        numSpread = num
        maxSpreadDistance = maxDistance
        return this
    }

    fun update(): List<Vec> {
        if (app.mousePressed || !onMousePressed) {
            val mouse = Vec(app.mouseX, app.mouseY)
            if (squareDistanceToLast(mouse) > minDistanceSquared) {
                if (numSpread == 0)
                    list.add(mouse)
                else {
                    val spread = List(numSpread) {
                        mouse + getSpreadPoint()
                    }
                    list.addAll(spread)
                }
            }
        }
        return list
    }

    private fun getSpreadPoint(): Vec {
        val distance = maxSpreadDistance * Random.nextFloat()
        return Vec.fromRandomAngle() * distance
    }

    private fun squareDistanceToLast(vec: Vec): Float =
        if (list.isNotEmpty())
            list.last().squareDistance(vec)
        else
            Float.MAX_VALUE
}
