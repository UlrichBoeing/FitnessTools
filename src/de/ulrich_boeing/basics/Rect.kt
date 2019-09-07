package de.ulrich_boeing.basics

import de.ulrich_boeing.elements.Layout
import de.ulrich_boeing.processing.Look
import processing.core.PGraphics
import kotlin.math.ceil
import kotlin.math.floor

class Rect(var x: Float, var y: Float, width: Float, height: Float) {
    constructor(x: Int, y: Int, width: Int, height: Int) : this(
        x.toFloat(),
        y.toFloat(),
        width.toFloat(),
        height.toFloat()
    )

    constructor(other: Rect) : this(other.x, other.y, other.width, other.height)

    companion object {
        fun fromCircle(center: Vec, radius: Float): Rect {
            val p = center - Vec(radius, radius)
            return Rect(p.x, p.y, radius * 2, radius * 2)
        }
    }

    private var _width: Float = 0f
    private var _height: Float = 0f
    var width: Float
        get() = _width
        set(value) {
            if (value < 0f) {
                println("Width $value in Rect can not be negative.")
                _width = 0f
            } else

                _width = value
        }
    var height: Float
        get() = _height
        set(value) {
            if (value < 0f) {
                println("Height $value in Rect can not be negative.")
                _height = 0f
            } else
                _height = value
        }

    init {
        this.width = width
        this.height = height
    }

    var left: Float
        get() = x
        set(value) {
            println("value $value right $right")
            if (value < right) {
                width += value - x
                x = value
            } else
                println("hier liegt der Fehler")
        }
    var top: Float
        get() = y
        set(value) {
            height += value - y
            y = value
        }
    var right: Float
        get() = x + width
        set(value) {
            width = value - x
        }
    var bottom: Float
        get() = y + height
        set(value) {
            height = value - y
        }

    fun isOverlapping(other: Rect): Boolean {
        return !(x > other.right || right < other.x || y > other.bottom || bottom < other.y)
    }

    fun getEnclosingRectangle(other: Rect): Rect {
        val new = Rect(0, 0, 0, 0)
        new.left = if (left < other.left) left else other.left
        new.top = if (top < other.top) top else other.top
        new.right = if (right > other.right) right else other.right
        new.bottom = if (bottom > other.bottom) bottom else other.bottom
        return new
    }

    fun limit(other: Rect): Rect {
        val new = Rect(0, 0, 0, 0)
        if (!isOverlapping(other))
            return new

        new.left = if (other.left < left) left else other.left
        new.top = if (other.top < top) top else other.top
        new.right = if (other.right > right) right else other.right
        new.bottom = if (other.bottom > bottom) bottom else other.bottom

        return new
    }
}

fun Rect.debugDraw(g: PGraphics, accent: Boolean = false) {
    g.fill(255f, 40f)
    g.strokeWeight(2f)
    if (!accent)
        g.stroke(255f, 100f)
    else
        g.stroke(255f, 0f, 0f, 100f)
    g.rect(this.x, this.y, this.width, this.height)
}