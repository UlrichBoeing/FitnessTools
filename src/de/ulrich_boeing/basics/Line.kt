package de.ulrich_boeing.basics

class Line(val start: Vec, val end: Vec) {
    constructor(x1: Int, y1: Int, x2: Int, y2: Int) : this(Vec(x1, y1), Vec(x2, y2))

    fun tweenPoints(count: Int): List<Vec> {
        val list = mutableListOf<Vec>(start)
        val distance = end - start
        for (i in 1 until count - 1) {
            val tweenPoint = start + distance * ((i.toFloat()) / (count-1))
            list.add(tweenPoint)
        }
        list.add(end)
        return list
    }
}