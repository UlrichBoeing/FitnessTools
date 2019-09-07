package de.ulrich_boeing.elements

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.fitness_tools.ColorRange
import de.ulrich_boeing.fitness_tools.FloatRange
import de.ulrich_boeing.fitness_tools.VecRange
import processing.core.PConstants.ARGB
import processing.core.PGraphics
import processing.core.PImage
import kotlin.math.ceil


class Circle(val center: Vec, val diameter: Float, val color: Color) : Element {
    constructor() : this(vecRange.random(), radiusRange.random(), colorRange.random())

    companion object {
        val vecRange = VecRange(Vec(0, 0), Vec(1200, 900))
        val radiusRange = FloatRange(3f, 10f)
        val colorRange = ColorRange(
            Color.fromRGBA(128, 200, 64, 255),
            Color.fromRGBA(255, 255, 200, 255)
        )


    }

    init {
        createImage()
    }


    var strokeWeight = 0f
    override var fitness = 0.0
    // regelt die Häufigkeit der Mutation, abhängig z.B. von Fitness (schlechte Elemnente laufen früher ab), Größe (Grosse Elemente mutieren seltener)
    val expiry: Long = 0L
    override lateinit var image: PImage

    override val clipping: Clipping
        // TODO: Problem rect to big if circle is outside
        get() = Clipping.aroundRect(Rect.fromCircle(center, diameter / 2))


    override fun mutate(): Circle {
        val newCenter = vecRange.mutate(center, 30f)
        val newRadius = radiusRange.mutate(diameter, 30f)
        val newColor = colorRange.mutate(color, 30f)
        val newCircle = Circle(newCenter, newRadius, newColor)
        newCircle.strokeWeight = strokeWeight
        return newCircle
    }

    override fun mutate(amount: Float): Circle {
        return lerp(Circle(), amount)
    }

    fun lerp(other: Circle, amount: Float): Circle {
        val newCenter = center.lerp(other.center, amount)
        val newDiameter = lerp(diameter, other.diameter, amount)
        val newColor = color.lerp(other.color, amount)
        return Circle(newCenter, newDiameter, newColor)
    }

    override fun draw(g: PGraphics) {
        if (strokeWeight == 0f)
            g.noStroke()
        else {
            g.stroke(255)
            g.strokeWeight(12f)
        }
        g.fill(color.rgba)
        g.ellipse(center.x, center.y, diameter, diameter)
        g.fill(255f, 255f)
        val str = fitness.toString()
//        g.text(str, center.x, center.y)

//        g.fill(255f, 50f)
//        g.rect(rect.x, rect.y, rect.width, rect.height)
    }

    fun createImage() {
        val g = Layout.app.createGraphics(clipping.width, clipping.height)
        g.beginDraw()
        if (strokeWeight == 0f)
            g.noStroke()
        else {
            g.stroke(255)
            g.strokeWeight(4f)
        }
        g.fill(color.rgba)
        g.ellipse(clipping.width / 2f, clipping.height / 2f, diameter, diameter)
        g.endDraw()
        image = g.get()


    }

    override fun drawWithOffset(g: PGraphics, offset: Vec) {
        if (strokeWeight == 0f)
            g.noStroke()
        else {
            g.stroke(255)
            g.strokeWeight(4f)
        }
        g.fill(color.rgba)
        g.ellipse(center.x - offset.x, center.y - offset.y, diameter, diameter)

//        g.fill(255f, 50f)
//        g.rect(rect.x, rect.y, rect.width, rect.height)
    }


}
