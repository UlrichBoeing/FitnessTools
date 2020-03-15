package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.Vec
import de.ulrich_boeing.basics.setAlpha
import de.ulrich_boeing.drawables.Drawable
import de.ulrich_boeing.drawables.DrawableData
import processing.core.PGraphics

class SimpleCircle(position: Vec, data: DrawableData): Drawable(position, data) {
    override fun draw(g: PGraphics, size : Float) {
        val color = data.color.setAlpha(data.maxOpacity)
        g.fill(color)
        g.noStroke()
        g.scale(size)
        g.ellipse(position.x , position.y, data.size , data.size)
    }
}