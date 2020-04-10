package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.Point
import de.ulrich_boeing.basics.Vec
import de.ulrich_boeing.basics.toPoint
import de.ulrich_boeing.canvas.CanvasLayer

class CompareGrid(val points : List<Point>, val comparePoint: Point) {
    val size = points.size
    val comparePoints = points.toComparePoints(comparePoint)
    val minRGBDif = comparePoints.minBy { it.rgbDif }!!.rgbDif
    val maxRGBDif = comparePoints.maxBy { it.rgbDif }!!.rgbDif
    val rangeRGBDif = maxRGBDif - minRGBDif

    val normRGBDif = FloatArray(size) { i -> (comparePoints[i].rgbDif - minRGBDif).toFloat() / rangeRGBDif }

}