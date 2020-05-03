package de.ulrich_boeing.quadtree

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.framework.drawAsCircles
import processing.core.PGraphics

class Quadtree(val rect: Rect) {
    val capacity = intArrayOf(0, 0, 1, 2, 4, 8, 16, 32, 256, 50000)

    //    val capacity = intArrayOf(2, 2, 2, 2, 2)
    val clearParentNode = false
    var maxLevel = 0
    fun updateMaxLevel(level: Int) {
        if (level > maxLevel)
            maxLevel = level
    }

    private val root = Node(this, rect, 0)
    fun insert(vec: Vec): Boolean = root.add(vec)
    fun draw(g: PGraphics) = root.draw(g)
    fun drawPoints(g: PGraphics, radius: Float) = root.drawPoints(g, radius)
    fun query(queryRect: Rect): List<Vec> {
        val list = mutableListOf<Vec>()
        root.query(queryRect, list)
        return list
    }
    fun queryRects(queryRect: Rect): List<Rect> {
        val inside = mutableListOf<Rect>()
        val toCheck = mutableListOf<Rect>()
        root.queryRects(queryRect, inside, toCheck)
        return toCheck
    }

}

private class Node(val tree: Quadtree, val rect: Rect, val level: Int) : Rect(rect.x, rect.y, rect.width, rect.height) {
    val points = mutableListOf<Vec>()
    var isCleared: Boolean = false
    var children = Array<Node?>(4) { null }
    val hasChildren: Boolean
        //        get() = (points.size == tree.capacity)
        get() = (children[0] != null)


    init {
        tree.updateMaxLevel(level)
    }

    fun add(vec: Vec): Boolean {
        if (!rect.contains(vec))
            return false

        if (points.size < tree.capacity[level] && !isCleared) {
            points.add(vec)
        } else {
            if (!hasChildren)
                createChildren()
            if (tree.clearParentNode) {
                for (p in points)
                    addToChild(p)
                points.clear()
                isCleared = true
            }
            addToChild(vec)
        }
        return true
    }

    fun addToChild(vec: Vec) {
        for (child in children) {
            if (child!!.add(vec))
                break
        }
    }

    private fun createChildren() {
        val childWidth = rect.width / 2
        val childHeight = rect.height / 2
        val upper1 = Rect(rect.x, rect.y, childWidth, childHeight)
        val upper2 = Rect(rect.x + childWidth, rect.y, childWidth, childHeight)
        val lower1 = Rect(rect.x, rect.y + childHeight, childWidth, childHeight)
        val lower2 = Rect(rect.x + childWidth, rect.y + childHeight, childWidth, childHeight)
        children[0] = Node(tree, upper1, level + 1)
        children[1] = Node(tree, upper2, level + 1)
        children[2] = Node(tree, lower1, level + 1)
        children[3] = Node(tree, lower2, level + 1)
    }

    fun queryRects(queryRect: Rect, inside: MutableList<Rect>, toCheck: MutableList<Rect>) {
        when {
            !queryRect.intersects(rect) -> return
            queryRect.inside(rect)  -> inside.add(rect)
            points.size > 0  -> toCheck.add(rect)
//            else -> toCheck.add(rect)
        }
        if (hasChildren) {
            for (child in children)
                child!!.queryRects(queryRect, inside, toCheck)
        }
    }

    fun query(queryRect: Rect, list: MutableList<Vec>) {
        if (!queryRect.intersects(rect))
            return

        if (queryRect.inside(rect))
            copyAll(list)
        else {
            for (point in points) {
                if (queryRect.contains(point))
                    list.add(point)
            }
            if (hasChildren) {
                for (child in children)
                    child!!.query(queryRect, list)
            }
        }
    }

    fun copyAll(list: MutableList<Vec>) {
        list.addAll(points)
        if (hasChildren) {
            for (child in children)
                child!!.copyAll(list)
        }

    }

    fun draw(g: PGraphics) {
        if (points.size > 0) {
//            g.fill(COLOR_BLUE.setAlpha(points.size * 3 * level))
            rect.draw(g)
        }
        if (hasChildren) {
            for (child in children)
                child!!.draw(g)
        }
    }

    fun drawIntersection(g: PGraphics, other: Rect) {
        if (!(rect intersects other))
            return

        rect.draw(g)
        if (hasChildren) {
            for (child in children)
                child!!.drawIntersection(g, other)
        }
    }

    fun drawPoints(g: PGraphics, radius: Float) {
        points.drawAsCircles(g, radius)
        if (hasChildren) {
            for (child in children)
                child!!.drawPoints(g, radius / 2)
        }
    }


}


