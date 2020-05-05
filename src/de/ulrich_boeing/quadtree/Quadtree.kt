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
    var countNodes = 0
    private val root = Node(this, rect, 0)
    // deepest Level of all nodes
    var maxLevel = 0

    init {
        list.forEach { insert(it) }
    }

    fun updateStats(nodeLevel: Int) {
        countNodes++
        maxLevel = max(nodeLevel, maxLevel)
    }

    /**
     * Insert vec to Quadtree by inserting into root node
     * Insert only if vec is inside rect
     */
    fun insert(vec: Vec) {
        if (vec inside rect)
            root.insert(vec)
    }

    /**
     * Search for vectors inside a given rect
     */
    fun queryRect(queryRect: Rect): List<Vec> {
        val found = mutableListOf<Vec>()
        root.query(queryRect, found)
        return found
    }

    /**
     * Search for vectors inside a given circle
     */
    fun queryCircle(queryCircle: Circle): List<Vec> {
        val found = mutableListOf<Vec>()
        root.query(queryCircle, found)
        return found
    }

    fun draw(g: PGraphics) = root.draw(g)
    fun drawPoints(g: PGraphics, radius: Float) = root.drawPoints(g, radius)

    private fun sortNodes(queryRect: Rect): Pair<List<Node>, List<Node>> {
        val inside = mutableListOf<Node>()
        val toCheck = mutableListOf<Node>()
        root.sortNodes(queryRect, inside, toCheck)
        return Pair(inside, toCheck)
    }

    fun sortAndQuery(queryRect: Rect): List<Vec> {
        val (inside, toCheck) = sortNodes(queryRect)
        val found = mutableListOf<Vec>()
        inside.forEach { found.addAll(it.points) }
        toCheck.forEach { found.addAll(it.points.filter { vec -> queryRect.contains(vec) }) }
        return found
    }

    fun sortRects(queryRect: Rect): Pair<List<Rect>, List<Rect>> {
        val (inside, toCheck) = sortNodes(queryRect)
        val insideRects = List(inside.size) { i -> inside[i].rect }
        val toCheckRects = List<Rect>(toCheck.size) { i -> toCheck[i].rect }
        return Pair(insideRects, toCheckRects)
    }

    fun getNumberOfVecs(queryRect: Rect): Int {
        val (inside, toCheck) = sortNodes(queryRect)
        var sum = 0
        sum += inside.sumBy { it.points.size }
        for (node in toCheck) {
            for (vec in node.points)
                if (queryRect.contains(vec))
                    sum++
        }
        return sum
    }

    fun estimateNumberOfVecs(queryRect: Rect): Int {
        val (inside, toCheck) = sortNodes(queryRect)
        var sum = 0
        sum += inside.sumBy { it.points.size }
        sum += (toCheck.sumBy { it.points.size }) / 2
        return sum
    }

}

/**
 * Ein Child-Node wird nur erzeugt wenn ein Punkt hinzugefügt wird
 * (Aufruf von createChild() nur in insertToChild())
 */
private class Node(val tree: Quadtree, val rect: Rect, val level: Int) {
    var points = mutableListOf<Vec>()
    val capacity = tree.capacity[level]
    val lastLevel = (level == tree.capacity.lastIndex)
    val center = rect.center
    var children = Array<Node?>(4) { null }


    init {
        tree.updateStats(level)
    }

    fun insert(vec: Vec) {
        if (!rect.contains(vec))
            throw RuntimeException("Quadtree/Node: A vector to add must be in rect")

        if (points.size < capacity || lastLevel) {
            points.add(vec)
        } else {
            insertToChild(vec)
        }
    }

    fun insertToChild(vec: Vec) {
        val index = getChildIndex(vec)
        if (children[index] == null)
            children[index] = createChild(index)

        children[index]!!.insert(vec)
    }

    fun getChildIndex(vec: Vec): Int {
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

    fun sortNodes(queryRect: Rect, inside: MutableList<Node>, toCheck: MutableList<Node>) {
        when {
            rect outside queryRect -> return
            rect inside queryRect -> addAllNodes(inside)
            else -> {
                if (points.size > 0)
                    toCheck.add(this)
                children.forEach { it?.sortNodes(queryRect, inside, toCheck) }
            }
        }
    }

    fun addAllNodes(inside: MutableList<Node>) {
        if (points.size > 0)
            inside.add(this)
        children.forEach { it?.addAllNodes(inside) }
    }

    fun query(queryRect: Rect, found: MutableList<Vec>){
        when {
            rect outside queryRect -> return
            rect inside queryRect -> addAllPoints(found)
            else -> {
                for (point in points) {
                    if (queryRect.contains(point))
                        found.add(point)
                }
                children.forEach { it?.query(queryRect, found) }
            }
        }
    }

    fun query(queryCircle: Circle, found: MutableList<Vec>) {
        when {
            !(queryCircle.intersects(rect)) -> return
            rect inside queryCircle -> addAllPoints(found)
            else -> {
                for (point in points) {
                    if (queryCircle.contains(point))
                        found.add(point)
                }
                children.forEach { it?.query(queryCircle, found) }
            }
        }
    }

    fun addAllPoints(found: MutableList<Vec>) {
        found.addAll(points)
        children.forEach { it?.addAllPoints(found) }
    }


    fun draw(g: PGraphics) {
        rect.draw(g)
        children.forEach { it?.draw(g) }
    }

    fun drawPoints(g: PGraphics, radius: Float) {
        points.drawAsCircles(g, radius)
        children.forEach { it?.drawPoints(g, radius) }
    }
}
