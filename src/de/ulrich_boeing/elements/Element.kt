package de.ulrich_boeing.elements

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.basics.Rect
import de.ulrich_boeing.basics.Vec
import processing.core.PGraphics
import processing.core.PImage

interface Element {
    var image: PImage
    val clipping: Clipping
    var fitness : Double

//    val timeOfBirth: Long
    val size: Long
        get() = clipping.width.toLong() * clipping.height.toLong()



    fun mutate(amount: Float): Element
    fun mutate(): Element
    fun draw(g: PGraphics)
    fun drawWithOffset(g: PGraphics, offset: Vec)
}