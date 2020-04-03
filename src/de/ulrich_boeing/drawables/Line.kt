package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.framework.*
import processing.core.PGraphics

typealias ComparePoints = MutableList<ComparePoint>

class Line(position: Vec, data: DrawableData) : Drawable(position, data) {
    val color = canvasLayer.getColor(position)
    val point = Point(position, color)
    val polygonPoints = createPolygon(40).times(100f).plusToList(position)
    var endPoints = mutableListOf<Point>()
    var grid = createGridFromCenter(position, 200f, 200f, 12f).toPoints(canvasLayer).toComparePoints(point)
        .filter { it.distanceTo < 100f }
    var countSlices = 8
    var slices =  MutableList<ComparePoints>(countSlices) { mutableListOf<ComparePoint>() }
    init {
        grid = grid.sortedBy { it.rgbDif }
        grid.setColor(COLOR_RED, COLOR_BLUE)
        for (cp in grid) {
            val index = (cp.angleTo / Vec.TAU * countSlices).toInt()
            slices[index].add(cp)
        }
        for (slice in slices) {
//            slice.setColor(Int.randomColor(), COLOR_RED)
        }

//        for (p in polygonPoints) {
//            val lineVecs = createLine(position, p, 2f)
//            val linePoints = canvasLayer.getColors(lineVecs.subList(5, 49))
//            endPoints.add(linePoints.firstDif(color, 100))
        }
//    }

    fun getMaxColorDifPoint(start: Vec, end: Vec, steps: Int): Point {
        val lineVecs: List<Vec> = start.lerpList(end, steps)
        val linePoints = canvasLayer.getColors(lineVecs)
        val colorDifs: IntArray = linePoints.colorDifToNext()
        val maxColorDif = colorDifs.indexOfMax()
        return linePoints[maxColorDif]
    }

    override fun draw(g: PGraphics, size: Float) {

//        val color = data.color.setAlpha(data.maxOpacity)
        g.fill(color.setAlpha(data.maxOpacity))
        g.fill(COLOR_RED.setAlpha(30))
        g.noStroke()
        g.scale(size)
//    val filterGrid = grid.filter { it.angle > 0 && it.angle < Vec.PI / 2 }
//    position.drawAsCircle(g, 40f)


        g.fill(color.setAlpha(data.maxOpacity))
        g.noStroke()
        grid.drawAsCircles(g)
//    endPoints.drawAsCurvedShape(g)

    }


}