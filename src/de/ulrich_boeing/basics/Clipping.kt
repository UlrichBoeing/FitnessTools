package de.ulrich_boeing.basics

import de.ulrich_boeing.elements.Layout
import de.ulrich_boeing.processing.Look
import processing.core.PGraphics
import java.lang.RuntimeException
import kotlin.math.*

/**
 * x, y are located on the rectangle (and thus left, top).
 * Right, bottom are *outside* the rectangle to ensure same operations like in rect.
 */
class Clipping(var x: Int, var y: Int, width: Int, height: Int) {
    constructor () : this(Int.MIN_VALUE, Int.MIN_VALUE, 0, 0)

    companion object {
        fun fromVertices(left: Int, top: Int, right: Int, bottom: Int) = Clipping(left, top, right - left, bottom - top)

        fun aroundRect(rect: Rect, margin: Int = 0): Clipping {
            val x = floor(rect.x).toInt() - margin
            val y = floor(rect.y).toInt() - margin
            val right = ceil(rect.right).toInt() + margin
            val bottom = ceil(rect.bottom).toInt() + margin
            return Clipping(x, y, right - x, bottom - y)
        }
    }

    var width = width
        set(value) {
            field = if (value > 0) value else 0
        }

    var height = height
        set(value) {
            field = if (value > 0) value else 0
        }

    var left: Int
        get() = x
        set(value) {
            val limitedValue = if (value < right) value else right
            width -= limitedValue - x
            x = limitedValue
        }

    var top: Int
        get() = y
        set(value) {
            val limitedValue = if (value < bottom) value else bottom
            height -= limitedValue - y
            y = limitedValue
        }

    var right: Int
        get() = x + width
        set(value) {
            val limitedValue = if (value > left) value else left
            width = limitedValue - x
        }

    var bottom: Int
        get() = y + height
        set(value) {
            val limitedValue = if (value > top) value else top
            height = limitedValue - y
        }

    val center: Vec
        get() = Vec(x + width / 2f, y + height / 2f)

    // ok
    val vertices: Array<Point>
        get() = arrayOf(Point(x, y), Point(right, y), Point(x, bottom), Point(right, bottom))

    // ok
    fun contains(p: Point): Boolean {
        return !(p.x < x || p.x > right || p.y < y || p.y > bottom)
    }

    fun contains(clipping: Clipping): Boolean {
        return (containsVertices(clipping) == 4)
    }

    /**
     * number of vertices of clipping inside this
     */
    fun containsVertices(clipping: Clipping): Int {
        var numberVertices = 0
        for (p in clipping.vertices)
            if (this.contains(p))
                numberVertices++

        return numberVertices
    }

    fun exactUnion(other: Clipping): Array<Clipping> {
        if (!isOverlapping(other))
            return arrayOf(this, other)

        if (contains(other)) {
//            println("other inside")
            return arrayOf(this)
        }

        if (other.contains(this)) {
//            println("this inside")
            return arrayOf(other)
        }

        val sumOther = this.containsVertices(other)
        val sumThis = other.containsVertices(this)

        if (sumThis == 0 && sumOther == 0) {
//            println("splitMiddle(other)")
            return arrayOf(this) + other.splitMiddle(intersect(other))
        }

        if (sumThis == 2) {
//            println("this in zwei Teile aufteilen.")
            return arrayOf(other) + this.splitHalf(other.intersect(this))
        }

        if (sumOther == 2) {
//            println("other in zwei Teile aufteilen")
            return arrayOf(this) + other.splitHalf(intersect(other))
        }

        if (sumOther == 1 && sumThis == 1) {
            println("other in drei Teile aufteilen")
            return arrayOf(this) + other.splitCorner(intersect(other))
        }

        throw RuntimeException("Unexpected arrangement of rectangles.")
        return arrayOf(this, other)
    }

    // splits this in the middle defined by other
    fun splitMiddle(other: Clipping): Array<Clipping> {
        if (left < other.left) {
            return arrayOf(
                fromVertices(left, top, other.left, bottom),
                fromVertices(other.right, top, right, bottom)
            )
        } else
            return arrayOf(
                fromVertices(left, top, right, other.top),
                fromVertices(left, other.bottom, right, bottom)
            )
    }

    fun splitHalf(other: Clipping): Array<Clipping> {
        if (left == other.left && right != other.right)
            return arrayOf(Clipping.fromVertices(other.right, top, right, bottom))
        else if (left != other.left && right == other.right)
            return arrayOf(Clipping.fromVertices(left, top, other.left, bottom))
        else if (top == other.top && bottom != other.bottom)
            return arrayOf(Clipping.fromVertices(left, other.bottom, right, bottom))
        else
            return arrayOf(Clipping.fromVertices(left, top, right, other.top))

    }

    fun splitCorner(other: Clipping): Array<Clipping> {
        if (left == other.left && top == other.top)
            return arrayOf(
                fromVertices(left, other.bottom, other.right, bottom),
                fromVertices(other.right, top, right, bottom)
            )
        else if (left == other.left && bottom == other.bottom)
            return arrayOf(
                fromVertices(left, top, other.right, other.top),
                fromVertices(other.right, top, right, bottom)
            )
        else if (right == other.right && top == other.top)
            return arrayOf(
                fromVertices(left, top, other.left, bottom),
                fromVertices(other.left, other.bottom, right, bottom)
            )
        else
            return arrayOf(
                fromVertices(left, top, other.left, bottom),
                fromVertices(other.left, top, right, other.top)

            )

        return arrayOf(other)
    }

    fun isOverlapping(other: Clipping): Boolean {
        return !(x >= other.right || right <= other.x || y >= other.bottom || bottom <= other.y)
    }

    fun isOverlapping(others: Array<Clipping>): Boolean {
        for (other in others) {
            if (isOverlapping(other))
                return true
        }
        return false
    }

    fun intersect(other: Clipping): Clipping {
        val intersect = Clipping()
        if (!isOverlapping(other)) return intersect

        intersect.x = max(x, other.x)
        intersect.y = max(y, other.y)
        intersect.right = min(right, other.right)
        intersect.bottom = min(bottom, other.bottom)
        return intersect
    }

    fun union(other: Clipping): Clipping {
        val union = Clipping()
        union.x = min(x, other.x)
        union.y = min(y, other.y)
        union.right = max(right, other.right)
        union.bottom = max(bottom, other.bottom)
        return union
    }

    override fun equals(other: Any?): Boolean {
        if (this == other) return true
        if (other?.javaClass != javaClass) return false

        other as Clipping
        return !(x != other.x || y != other.y || width != other.width || height != other.height)
    }

    override fun toString(): String {
        return "Clipping(x=$x, y=$y, width=$width, height=$height)"
    }

    fun draw(look: Look, pGraphics: PGraphics? = null) {
        val g = pGraphics ?: Layout.app.g
        look.set(g)
        g.rect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    }


}

class PointClipping(var x1: Int, var y1: Int, var x2: Int, var y2: Int) {
    val clipping: Clipping
        get() = Clipping(x, y, width, height)
    val x: Int
        get() = min(x1, x2)
    val y: Int
        get() = min(y1, y2)
    val width: Int
        get() = (x2 - x1).absoluteValue
    val height: Int
        get() = (y2 - y1).absoluteValue
    val left: Int
        get() = min(x1, x2)
    val right: Int
        get() = max(x1, x2)
    val top: Int
        get() = min(y1, y2)
    val bottom: Int
        get() = max(y1, y2)
}
