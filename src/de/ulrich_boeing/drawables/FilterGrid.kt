package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.extensions.*
import de.ulrich_boeing.framework.*
import processing.core.PGraphics
import kotlin.math.roundToInt


fun FloatArray.invert(): FloatArray = FloatArray(size) { i -> 1 - this[i] }
fun FloatArray.expand(from: Int, to: Int): IntArray = IntArray(size) { i -> (this[i] * (to - from) + to).roundToInt() }

class FilterGrid(position: Vec, data: DrawableData) : Drawable(position, data) {
    var center = position.toPoint(canvasLayer)
    var pointGrid = createGridFromCenter(position, 200f, 200f, 4f).toPoints(canvasLayer)

    var grid = CompareGrid(pointGrid, center)


    val width: Int = 9
    val centerPos: Int = width / 2
    val list = mutableListOf<Point>()
//    val angles: List<List<ComparePoint>>
//    val p: List<ComparePoint>

    //    val pointList : List<Point>
    init {
//        println("rgbDif-mean of Grid: " + grid.rgbDifs.stdDeviation)
//        val rings = grid.distances.normValues.repeat(25).sin().withWeight(0.41f)
        val values = combine(
            grid.rgbDifs().withWeight(1f),
            grid.distances().withWeight(2f)
        )
        val alpha = values.invert().exp(6f).expandToInt()


        for (i in grid.comparePoints.indices) {
            grid.comparePoints[i].color = grid.comparePoints[i].color.setAlpha(alpha[i])
//            alpha = if (alpha < 210) 0 else alpha
//            pointGrid[i].color = center.color.setAlpha(alpha[i])
        }

//        angles = grid.comparePoints.splitAngles(16)
//        p  = List(angles.size) { i-> getFirstAlpha(angles[i], 10)}
//        p.setColor(COLOR_BLUE)


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

    fun getFirstAlpha(list: List<ComparePoint>, alphaLimit: Int): ComparePoint {
        val sortedList = list.sortedBy { -it.distanceTo }
        return sortedList.firstOrNull { it.color.alpha > alphaLimit } ?: sortedList[0]
    }

    override fun draw(g: PGraphics, size: Float) {
        g.scale(size)
//        grid.comparePoints.drawAsCircles(g, 4f)
        g.fill(center.color.setAlpha(40))
//        g.noStroke()
//        p.drawAsShape(g)
//        nearBy.setColor(COLOR_GREEN)
//        nearBy.drawAsCircles(g, 3f)
        for (cp in grid.comparePoints) {
            var nearBy = grid.comparePoints.getNearby(cp, 10f)
            nearBy = nearBy.filter { it.color.alpha > 5 }
            cp.color = center.color.setAlpha(cp.color.alpha).mixColor(cp.color, 0.2f).mixColor(data.color.setAlpha(30), 0.0f)
            println("nearBY.size" + nearBy.size)
            cp.drawAsCircle(g,  0.5f * nearBy.size)
        }
//        grid.comparePoints.drawAsCircles(g, 10f)
//        cp.drawAsCircle(g, 15f)
//        angles[0].drawAsCircles(g, 4f)
//        list.drawAsCircles(g, 6f)
    }
}