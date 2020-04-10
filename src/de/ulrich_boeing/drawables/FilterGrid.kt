package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.framework.*
import processing.core.PGraphics

class FilterGrid(position: Vec, data: DrawableData) : Drawable(position, data) {
    var center = position.toPoint(canvasLayer)
    var grid = createGridFromCenter(position, 50f, 50f, 12f).toPoints(canvasLayer)

    var compareGrid = CompareGrid(grid, center)


    val width: Int = 9
    val centerPos: Int = width / 2
    val list = mutableListOf<Point>()

    //    val pointList : List<Point>
    init {
        for (i in grid.indices) {
            var alpha = 255 - (compareGrid.normRGBDif[i] * 255).toInt()
            alpha = if (alpha < 128) 0 else alpha

            val color = center.color.setAlpha(alpha / 2)
            grid[i].color = color
        }

//        val i = 2
//        val t = i -1
//        for (x in (centerPos - i)..(centerPos + i)) {
//            list.add(grid[x + (centerPos - i) * width])
//        }
//        for (y in (centerPos - t)..(centerPos + t)) {
//            list.add(grid[(centerPos - i) + y * width])
//        }
//        for (y in (centerPos - t)..(centerPos + t)) {
//            list.add(grid[(centerPos + i) + y * width])
//        }
//        for (x in (centerPos - i)..(centerPos + i)) {
//            list.add(grid[x + (centerPos + i) * width])
//        }
//        list.setColor(COLOR_YELLOW)
//        pointList = list.toPoints(can vasLayer)
//        pointList.setColor(COLOR_RED)
    }

    override fun draw(g: PGraphics, size: Float) {
        g.scale(size)
        grid.drawAsCircles(g, 5f)
//        list.drawAsCircles(g, 6f)
    }
}