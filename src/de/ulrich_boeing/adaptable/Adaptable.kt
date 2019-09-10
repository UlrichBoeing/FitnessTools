package de.ulrich_boeing.adaptable

import com.sun.javafx.scene.control.skin.VirtualFlow
import de.ulrich_boeing.basics.*
import de.ulrich_boeing.fitness_tools.FloatRange
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

typealias DNA = FloatArray
typealias Structure = Array<Vec>

class PopulationStats(val bestFitness: Float, val worstFitness: Float)

class AdaptableStats() {
    val populationStats = ArrayList<PopulationStats>(200)
    val history: Queue<PopulationStats> = ArrayDeque<PopulationStats>()

    fun test() {
        history.add(PopulationStats(0f, 0f))
        if (history.size == 10)
            history.remove()
        for (item in history) {
            println(item.bestFitness)
            history.remove(item)
        }
    }


    var generations: Int = 0
    var bestFitness = -1f
}

class Adaptable(val app: PApplet, val clipping: Clipping, val populationCount: Int = 1) {
    /*
        Fixed parameter of Adaptable
    */
    val pointsCount = 6
    val radiusRange = FloatRange(0.1f, 0.95f)
    val color = Color.fromRGBA(0, 0, 255, 180)

    /*
        parameter of the genetic algorithm
    */
    private val sizeDNA = 7
    var population = initPopulation()
    var mutationRate = 0.06f
    var mutationRange = 0.02f
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    val stats = AdaptableStats()


    // Cache Variables
    val angleStepSize = Vec.TAU / pointsCount
    var allNormStructures = createAllNormStructures()
    var fitness = FloatArray(populationCount)

    fun createAllNormStructures(): Array<Structure> = Array(populationCount) { i -> createNormStructure(population[i]) }

    fun createNormStructure(dna: DNA): Structure {
        val start = angleStepSize * (dna.last() - 0.5f)
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

    fun expandAllStructures(scale: Float): Array<Structure> =
        Array<Structure>(populationCount) { i -> expandStructure(allNormStructures[i], scale) }

    fun expandStructure(normStructure: Structure, scale: Float): Structure {
        val r1 = (clipping.width * scale) / 2f
        val r2 = (clipping.height * scale) / 2f
        return Array(pointsCount) { i ->
            Vec(normStructure[i].x * r1, normStructure[i].y * r2)
        }
    }

    fun getImage(structure: Structure, targetClipping: Clipping): PGraphics {
        val width = targetClipping.width
        val height = targetClipping.height
        val g = app.createGraphics(width, height)
        g.beginDraw()
        g.background(0)
        g.noStroke()

        g.translate(width / 2f, height / 2f)
        g.fill(color.rgba)
        g.beginShape()
//        curveVertex(center.x + vecs.last().x, center.y + vecs.last().y)
        for (i in 1..2)
            for (point in structure) {
                g.curveVertex(point.x, point.y)
            }
//        curveVertex(center.x + vecs.first().x, center.y + vecs.first().y)
//        curveVertex(center.x + vecs[2].x, center.y + vecs[2].y)
        g.endShape()
        g.endDraw()
        return g
    }

    fun getImage(scale: Float, index: Int = 0): PGraphics {
        val structure = expandStructure(allNormStructures[index], scale)
        return getImage(structure, clipping)
    }

    fun evaluateFitness(target: PImage, targetClipping: Clipping) {
        val scale = targetClipping.width / clipping.width.toFloat()
        val allStructures = expandAllStructures(scale)

        for (i in allStructures.indices) {
            val image = getImage(allStructures[i], targetClipping)
            val dif = target.getDifference(image)
            val colorValuesCount = image.width * image.height * 3 * 255
            fitness[i] = 1 - (dif / colorValuesCount.toFloat())

        }
//        val sortedFitness = fitness.sortedArray()
    }

    fun getBestFitness(): Int = fitness.indexOfMax()

    fun mutate(dna: DNA) {
        for (i in dna.indices) {
            if (Random.nextFloat() < mutationRate) {
                val new = Random.nextFloat()
                dna[i] = (1 - mutationRange) * dna[i] + mutationRange * new
            }
        }
    }

    private fun initDNA(): DNA = FloatArray(sizeDNA) { Random.nextFloat() }

    private fun initPopulation(): Array<DNA> = Array(populationCount) { initDNA() }

    private fun createDNA(): DNA {
        val i1 = fitness.selectOne()
        val i2 = fitness.selectOne()
        val newDNA = population[i1].mix(population[i2], Random.nextInt(sizeDNA - 1))
        mutate(newDNA)
        return newDNA
    }

    private fun createPopulation(): Array<DNA> = Array(populationCount) { createDNA() }

    private fun evaluatePopulation() {
        stats.populationStats.add(PopulationStats(fitness.max() ?: 0f, fitness.min() ?: 0f))
    }

    fun nextGeneration(target: PImage, targetClipping: Clipping) {
        evaluateFitness(target, targetClipping)
        evaluatePopulation()
        fitness.norm()
        population = createPopulation()
        stats.generations++
        allNormStructures = createAllNormStructures()
    }
}