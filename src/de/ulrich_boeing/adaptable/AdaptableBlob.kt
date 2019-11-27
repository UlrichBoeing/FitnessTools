package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.fitness_tools.FloatRange
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import kotlin.math.cos
import kotlin.math.sin

typealias Structure = Array<Vec>

class AdaptableBlob(app: PApplet, frame: Clipping) : Adaptable(app, frame) {
    /*
        Fixed parameter of Adaptable
    */
    val pointsCount = 6
    val radiusRange = FloatRange(0.3f, 1f)
    var color = Color.fromRGBA(0,0, 255, 255)

    override val sizeDNA = pointsCount + 1

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
        Flexible Parameter from DNA
    */
    // Cache Variables
    val angleStepSize = Vec.TAU / pointsCount
//    var allNormStructures = createAllNormStructures()

//    fun createAllNormStructures(): Array<Structure> =
//        Array(populationCount) { i -> createNormStructure(population[i]) }

    fun createNormStructure(dna: DNA): Structure {
        val start = angleStepSize * (dna[pointsCount] - 0.5f)
        val angles = FloatArray(pointsCount) { i ->
            start + i * angleStepSize
        }
        val lengths = FloatArray(pointsCount) { i ->
            radiusRange.expand(dna[i])
        }
        return Array(pointsCount) { i ->
            Vec(lengths[i] * cos(angles[i]), lengths[i] * sin(angles[i]))
        }
    }

//    fun expandAllStructures(scale: Float): Array<Structure> =
//        Array<Structure>(populationCount) { i -> oldExpandStructure(allNormStructures[i], scale) }

//    fun oldExpandStructure(normStructure: Structure, scale: Float): Structure {
//        val r1 = (frame.width * scale) / 2f
//        val r2 = (frame.height * scale) / 2f
//        return Array(pointsCount) { i ->
//            Vec(normStructure[i].x * r1, normStructure[i].y * r2)
//        }
//    }

    fun expandStructure(structure: Structure, r1: Float, r2: Float) {
        for (vec in structure) {
            vec.x *= r1
            vec.y *= r2
        }
    }

    override fun draw(dna: DNA, g: PGraphics) {
        val structure = createNormStructure(dna)
        expandStructure(structure, g.width / 2f, g.height / 2f)

        g.background(0)
        g.noStroke()
        g.fill(color.rgba)

        g.translate(g.width / 2f, g.height / 2f)
        g.beginShape()
//        curveVertex(center.x + vecs.last().x, center.y + vecs.last().y)
        for (i in 1..2)
            for (point in structure) {
                g.curveVertex(point.x, point.y)
            }
//        curveVertex(center.x + vecs.first().x, center.y + vecs.first().y)
//        curveVertex(center.x + vecs[2].x, center.y + vecs[2].y)
        g.endShape()
    }

//    fun oldGetImage(structure: Structure, targetClipping: Clipping): PGraphics {
//        val width = targetClipping.width
//        val height = targetClipping.height
//        val g = app.createGraphics(width, height)
//        g.beginDraw()
////        g.background(0)
////        g.image(target, 0f, 0f)
//
//        g.noStroke()
//        g.translate(width / 2f, height / 2f)
//        g.fill(color.rgba)
//        g.beginShape()
////        curveVertex(center.x + vecs.last().x, center.y + vecs.last().y)
//        for (i in 1..2)
//            for (point in structure) {
//                g.curveVertex(point.x, point.y)
//            }
////        curveVertex(center.x + vecs.first().x, center.y + vecs.first().y)
////        curveVertex(center.x + vecs[2].x, center.y + vecs[2].y)
//        g.endShape()
//        g.endDraw()
//
//        return g
//    }
//
//    fun oldGetImage(scale: Float, index: Int = 0): PGraphics {
//        allNormStructures = createAllNormStructures()
//        val structure = oldExpandStructure(allNormStructures[index], scale)
//        return oldGetImage(structure, frame)
//    }
//
//    fun oldEvaluateFitness(target: PImage, targetClipping: Clipping) {
//        val scale = targetClipping.width / frame.width.toFloat()
//        allNormStructures = createAllNormStructures()
//        val allStructures = expandAllStructures(scale)
//
//        for (i in allStructures.indices) {
//            val image = oldGetImage(allStructures[i], targetClipping)
//            val dif = target.getDifference(image)
//            val colorValuesCount = image.width * image.height * 3 * 255
//            fitness[i] = 1 - (dif / colorValuesCount.toFloat())
//
//        }
////        val sortedFitness = fitness.sortedArray()
//    }
//
//
////    private fun createPopulation(): Array<DNA> = Array(populationCount) { i -> createDNA(i) }
//
//    fun oldNextGeneration(target: PImage, targetClipping: Clipping) {
//        oldEvaluateFitness(target, targetClipping)
//        evaluatePopulation()
//        fitness.norm()
//        population = createPopulation()
//        stats.generations++
//        allNormStructures = createAllNormStructures()
//    }
}