import processing.core.PGraphics

fun PGraphics.differentSize(other: PGraphics): Boolean = (this.width != other.width) || (this.height != other.height)