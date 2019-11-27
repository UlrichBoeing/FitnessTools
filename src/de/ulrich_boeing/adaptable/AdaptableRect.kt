package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.basics.Color
import de.ulrich_boeing.basics.Rect
import de.ulrich_boeing.fitness_tools.ColorRange
import de.ulrich_boeing.fitness_tools.FloatRange
import processing.core.PApplet
import processing.core.PGraphics
import java.lang.Float.min
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

class AdaptableRect(app: PApplet, frame: Clipping) : Adaptable(app, frame) {
    val color = ColorRange(
        Color.fromRGBA(0, 0, 255, 255),
        Color.fromRGBA(255, 255, 255, 255))


    val sizeFactor = FloatRange(0.4f)
    val maxAspectRatio = 7f

    override val sizeDNA = 3
    override val creaturesCount = 1


    override fun draw(dna: DNA, g: PGraphics) {
        val clipping = getClipping(dna[0], dna[1], dna[2])
        val rect = Rect(clipping.x, clipping.y, clipping.width, clipping.height)
        g.noStroke()
        g.fill(color.start.rgba)

        g.rect(rect.x, rect.y, rect.width, rect.height)
    }

//    val sizeFactor: Float = sizeFactor / currentCreature.toFloat()

    val size: Float
        get() {
            return frame.size * sizeFactor.lerp(normCreaturesCount)
        }

    val minSideLength: Float
        get() = sqrt(size / maxAspectRatio)

    val maxSideLength: Float
        get() = sqrt(size * maxAspectRatio)

    val maxWidth: Float
        get() = min(maxSideLength, frame.width.toFloat())

    val maxHeight: Float
        get() = min(maxSideLength, frame.height.toFloat())

    fun getSizedClipping(normWidth: Float): Clipping {
        val minWidth = size / maxHeight
        val width = (1 - normWidth) * minWidth + normWidth * maxWidth
        val height = size / width
        return Clipping(0f, 0f, width, height)
    }

    fun getClipping(normWidth: Float, normX: Float, normY:Float): Clipping {
        val clipping = getSizedClipping(normWidth)
        clipping.x = (normX * (frame.width - clipping.width)).roundToInt()
        clipping.y = (normY * (frame.height - clipping.height)).roundToInt()
        return clipping
    }
}