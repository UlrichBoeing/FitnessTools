package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.ComparePoint
import de.ulrich_boeing.basics.Point
import processing.core.PApplet.sqrt
import kotlin.math.pow

class CompareGrid(val points : List<Point>, val comparePoint: Point) {
    val size = points.size
    val comparePoints = points.toComparePoints(comparePoint)

    val minRGBDif = comparePoints.minBy { it.rgbDif }!!.rgbDif
    val maxRGBDif = comparePoints.maxBy { it.rgbDif }!!.rgbDif
    val rangeRGBDif = maxRGBDif - minRGBDif
    val normRGBDif = FloatArray(size) { i -> (comparePoints[i].rgbDif - minRGBDif).toFloat() / rangeRGBDif }

    val rgbDifs = Stats(comparePoints,{ p: ComparePoint -> p.rgbDif.toFloat() })
    val distances = Stats(comparePoints, { p: ComparePoint -> p.distanceTo})
    val anglesTo = Stats(comparePoints, { p: ComparePoint -> p.angleTo })

    init {
        for (i in normRGBDif.indices)
            if (normRGBDif[i] != rgbDifs.normValues[i])
            throw RuntimeException("********************************************** ${normRGBDif[i]} : + ${rgbDifs.normValues[i]}")
    }

}
class Stats(list: List<ComparePoint>, myFun: (p: ComparePoint) -> Float) {
    val minElement = list.minBy { myFun(it) }
    val maxElement = list.maxBy { myFun(it) }
    val min = myFun(minElement!!)
    val max = myFun(maxElement!!)
    val range = max - min
    val normValues = FloatArray(list.size) { i -> (myFun(list[i]) - min) / range }
    val mean : Float = list.fold(0f) { sum, it -> sum + myFun(it) } / list.size
    val variance : Float = list.fold(0f) { sum, it -> sum + (myFun(it) - mean).pow(2f) } / list.size
    val stdDeviation = sqrt(variance)

    operator fun invoke() = normValues
}
