package de.ulrich_boeing.quadtree

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.framework.drawAsCircles
import processing.core.PGraphics

class IndexQuadtree(val list: List<Vec>, val rect: Rect, val capacity: Int) {


    val emptyParentNode = false
    var maxLevel = 0
    private val root = IndexNode(this, rect, 0)

    init {
        for (vec in list) {
            root.add(vec)
        }
    }
    fun checkMaxLevel(level: Int) {
        if (level > maxLevel)
            maxLevel = level
    }
//    fun insert(vec: Vec): Boolean {
//        list.add(vec)
//        return root.add(list.lastIndex)
//    }
    fun draw(g: PGraphics) = root.draw(g)
    fun drawPoints(g: PGraphics, radius: Float) = list.drawAsCircles(g, radius)
    fun query(queryRect: Rect): List<Vec> {
        val found = mutableListOf<Vec>()
        root.query(queryRect, found)
        return found
    }

}

private class IndexNode(val tree: IndexQuadtree, val rect: Rect, val level: Int) {
    val points = Array<Vec?>(tree.capacity) { null }  //mutableListOf<Vec>()
    var arrIndex = 0

    var isCleared: Boolean = false
    var children = Array<IndexNode?>(4) { null }
    val hasChildren: Boolean
        get() = (children[0] != null)

    init {
        tree.checkMaxLevel(level)
    }

    fun add(vec: Vec): Boolean {
        if (!rect.contains(vec))
            return false

        if (arrIndex < tree.capacity && !isCleared) {
            points[arrIndex] = vec
            arrIndex++
        } else {
            if (!hasChildren) {
                createChildren()
                if (tree.emptyParentNode) {
                    for (i in points.indices) {
                        addToChild(points[i]!!)
//                         points[i] = -1
                    }
                    isCleared = true
                }
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
        children[0] = IndexNode(tree, upper1, level + 1)
        children[1] = IndexNode(tree, upper2, level + 1)
        children[2] = IndexNode(tree, lower1, level + 1)
        children[3] = IndexNode(tree, lower2, level + 1)
    }

    fun query(queryRect: Rect, found: MutableList<Vec>) {
        if (!queryRect.intersects(rect))
            return

        for (i in 0 until arrIndex) {
            val vec = points[i]
            if (queryRect.contains(vec!!)) {
                found.add(vec)
            }
        }
        if (hasChildren) {
            for (child in children) {
                child!!.query(queryRect, found)
            }
        }
    }

    fun draw(g: PGraphics) {
        if (!isCleared && arrIndex > 0) {
//            g.fill(COLOR_BLUE.setAlpha(points.size * 3 * level))
        }
        rect.draw(g)
        if (hasChildren) {
            for (child in children)
                child!!.draw(g)
        }
    }

//    fun drawIntersection(g: PGraphics, other: Rect) {
//        if (!(rect intersects other))
//            return
//
//        rect.draw(g)
//        if (hasChildren) {
//            for (child in children)
//                child!!.drawIntersection(g, other)
//        }
//    }
//
//    fun drawPoints(g: PGraphics, radius: Float) {
//        points.drawAsCircles(g, radius)
//        if (hasChildren) {
//            for (child in children)
//                child!!.drawPoints(g, radius / 2)
//        }
//    }


}


