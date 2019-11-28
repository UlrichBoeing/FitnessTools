package de.ulrich_boeing.extensions

import processing.core.PImage
import kotlin.math.roundToInt

/*
    Needs valid coordinates and pixels loaded
 */
inline fun PImage.getPixel(x: Int, y: Int) = this.pixels[x + y * this.width]

/*
    Needs valid coordinates and pixels loaded
 */
inline fun PImage.getPixel(x: Float, y: Float) = this.getPixel(x.roundToInt(), y.roundToInt())


