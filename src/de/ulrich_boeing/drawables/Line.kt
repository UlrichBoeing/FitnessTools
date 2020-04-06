package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.framework.*
import processing.core.PGraphics

//typealias ComparePoints = MutableList<ComparePoint>/

class Line(position: Vec, data: DrawableData) : Drawable(position, data) {
    val color = canvasLayer.getColor(position)
    val point = Point(position, color)
    val polygonPoints = createPolygon(40).times(100f).plusToList(position)
    var endPoints = mutableListOf<Point>()
    var grid =
        createGridFromCenter(position, data.size * 6, data.size * 6, 2f).toPoints(canvasLayer).toComparePoints(point)
            .filter { it.distanceTo < data.size * 3f }
//            .filter { it.distanceTo > data.size * 0.5f }

    var countSlices = 5
    var slices = grid.splitAngles(countSlices).removeEmpty()
    val sortedSlices = List(slices.size) { i -> slices[i].sortedBy { it.distanceTo } }
    val maxDif = List<Int>(sortedSlices.size) { i -> sortedSlices[i].indexOfFirstDif(100) }
    var points = List<ComparePoint>(maxDif.size) { i -> slices[i][maxDif[i]] }

    init {
        for (i in 1..2) {
            val tweenPoints = points.tweenPointsOfShape().shiftInCircle(3f).toPoints(canvasLayer).toComparePoints(point)
            points = points.zipTweenPoints(tweenPoints)
        }

//        for (p in polygonPoints) {
//            val lineVecs = createLine(position, p, 2f)
//            val linePoints = canvasLayer.getColors(lineVecs.subList(5, 49))
//            endPoints.add(linePoints.firstDif(color, 100))
//        }
    }

    override fun draw(g: PGraphics, size: Float) {

//        val color = data.color.setAlpha(data.maxOpacity)
        g.fill(color.setAlpha(data.maxOpacity))
//        g.fill(COLOR_RED.setAlpha(30))
//        g.noStroke()
        g.scale(size)
//    val filterGrid = grid.filter { it.angle > 0 && it.angle < Vec.PI / 2 }
//    position.drawAsCircle(g, 40f)


//        g.fill(color.setAlpha(data.maxOpacity))
        g.noStroke()
//        grid.drawAsCircles(g)
//    endPoints.drawAsCurvedShape(g)
//        for (i in 0 until countSlices) {
//            slices[i][maxDif[i]].drawAsCircle(g)
//        }
//        val average = points.averageRGBDif()
//        points = points.filter { it.rgbDif < average }
        points.drawAsCurvedShape(g)
    }


}