package de.ulrich_boeing.fitness_tools

import de.ulrich_boeing.elements.Layout

class Stats(val layout: Layout) {
    val countElements: Int
        get() = layout.elements.size

    var drawTime: Long = 0L
    var evaluationTime: Long = 0L
    var drawAllTime: Long = 0L
    var drawAllCount: Int = 0




}