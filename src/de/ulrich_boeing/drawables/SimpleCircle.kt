package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.setAlpha
import de.ulrich_boeing.drawables.Drawable
import de.ulrich_boeing.drawables.DrawableData
import processing.core.PGraphics

class SimpleCircle(data: DrawableData): Drawable(data) {
    override fun draw(g: PGraphics, size : Float) {
        val color = data.color.setAlpha(data.maxOpacity)
        g.fill(color)
        g.noStroke()
        g.scale(size)
        g.ellipse(data.position.x , data.position.y, data.size , data.size)
    }
}