package de.ulrich_boeing.quadtree

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.framework.drawAsCircles
import processing.core.PGraphics
import java.lang.RuntimeException
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt

fun createIntArray(size: Int, factor: Float = 1f, startPos: Int = 1, startValue: Int = 1) = IntArray(size) { i ->
    if (i < startPos) 0
    else {
        val exponent = (i - startPos)
        (startValue * factor.pow(exponent)).roundToInt()
    }
}

class Quadtree(list: List<Vec>, val rect: Rect, val capacity: IntArray) {
    var countNodes: Int = 0
        get() = field
        private set(value) {
            field = value
        }

    private val root = Node(this, rect, 0)

    // deepest Level of all nodes
    var maxLevel: Int = 0
        get() = field
        private set(value) {
            field = value
        }

    init {
        list.forEach { insert(it) }
    }

    internal fun updateStats(nodeLevel: Int) {
        countNodes++
        maxLevel = max(nodeLevel, maxLevel)
    }

    /**
     * Insert vec to Quadtree by inserting into root node
     * Insert only if vec is inside rect
     */
    internal fun insert(vec: Vec) {
        if (vec inside rect)
            root.insert(vec)
    }

    /**
     * Search for vectors inside a given figure
     */
    fun pointsInside(figure: Figure): List<Vec> {
        val found = mutableListOf<Vec>()
        root.pointsInside(figure, found)
        return found
    }

    fun sortNodes(figure: Figure): Pair<List<Node>, List<Node>> {
        val inside = mutableListOf<Node>()
        val toCheck = mutableListOf<Node>()
        root.sortNodes(figure, inside, toCheck)
        return Pair(inside, toCheck)
    }

    fun draw(g: PGraphics) = root.draw(g)
    fun drawPoints(g: PGraphics, radius: Float) = root.drawPoints(g, radius)
}

/**
 * Ein Child-Node wird nur erzeugt wenn ein Punkt hinzugefügt wird
 * (Aufruf von createChild() nur in insertToChild())
 */
class Node(val tree: Quadtree, val rect: Rect, val level: Int) {
    var points = mutableListOf<Vec>()
    private val capacity = tree.capacity[level]
    private val lastLevel = (level == tree.capacity.lastIndex)
    private val center = rect.center
    private var children = Array<Node?>(4) { null }


    init {
        tree.updateStats(level)
    }

    internal fun insert(vec: Vec) {
        if (!rect.contains(vec))
            throw RuntimeException("Quadtree/Node: A vector to add must be in rect")

        if (points.size < capacity || lastLevel) {
            points.add(vec)
        } else {
            insertToChild(vec)
        }
    }

    private fun insertToChild(vec: Vec) {
        val index = getChildIndex(vec)
        if (children[index] == null)
            children[index] = createChild(index)

        children[index]!!.insert(vec)
    }

    private fun getChildIndex(vec: Vec): Int {
        val xIndex = if (vec.x > center.x) 1 else 0
        val yIndex = if (vec.y > center.y) 2 else 0
        return xIndex + yIndex
    }

    private fun createChild(index: Int): Node {
        val childWidth = rect.width / 2
        val childHeight = rect.height / 2
        return when (index) {
            0 -> Node(tree, Rect(rect.x, rect.y, childWidth, childHeight), level + 1)
            1 -> Node(tree, Rect(rect.x + childWidth, rect.y, childWidth, childHeight), level + 1)
            2 -> Node(tree, Rect(rect.x, rect.y + childHeight, childWidth, childHeight), level + 1)
            else -> Node(tree, Rect(rect.x + childWidth, rect.y + childHeight, childWidth, childHeight), level + 1)
        }
    }

    internal fun pointsInside(searchArea: Figure, found: MutableList<Vec>) {
        when {
            !searchArea.intersects(rect) -> return
            searchArea.contains(rect) -> addAllPoints(found)
            else -> {
                for (point in points) {
                    if (searchArea.contains(point))
                        found.add(point)
                }
                children.forEach { it?.pointsInside(searchArea, found) }
            }
        }
    }

    private fun addAllPoints(found: MutableList<Vec>) {
        found.addAll(points)
        children.forEach { it?.addAllPoints(found) }
    }

    internal fun sortNodes(figure: Figure, inside: MutableList<Node>, toCheck: MutableList<Node>) {
        when {
            !figure.intersects(rect) -> return
            figure.contains(rect) -> addAllNodes(inside)
            else -> {
                if (points.size > 0)
                    toCheck.add(this)
                children.forEach { it?.sortNodes(figure, inside, toCheck) }
            }
        }
    }

    private fun addAllNodes(inside: MutableList<Node>) {
        if (points.size > 0)
            inside.add(this)
        children.forEach { it?.addAllNodes(inside) }
    }

    internal fun draw(g: PGraphics) {
        rect.draw(g)
        children.forEach { it?.draw(g) }
    }

    internal fun drawPoints(g: PGraphics, radius: Float) {
        points.drawAsCircles(g, radius)
        children.forEach { it?.drawPoints(g, radius) }
    }
}
