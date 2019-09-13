package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.fitness_tools.ColorRange
import de.ulrich_boeing.fitness_tools.FloatRange
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

typealias DNA = FloatArray
typealias Structure = Array<Vec>

class PopulationStats(val bestFitness: Float, val worstFitness: Float)

class AdaptableStats() {
    val history = HistoryList<PopulationStats>(5)


    var generations: Int = 0
    var bestFitness = -1f
    var fitnessWorse = 0
    var fitnessEqual = 0

    fun update() {
        if (history.size > 1) {
            if (history[0].bestFitness < history[1].bestFitness)
                fitnessWorse++
            else if (history[0].bestFitness == history[1].bestFitness)
                fitnessEqual++
        }
    }
}

class Adaptable(val app: PApplet, val clipping: Clipping, val populationCount: Int = 1) {
    /*
        Fixed parameter of Adaptable
    */
    val pointsCount = 16
    val radiusRange = FloatRange(-0.2f, 1f)
    var color = Color.fromRGBA(255,0, 0, 255)

    /*
        parameter of the genetic algorithm
    */
    private val sizeDNA = pointsCount + 1
    var population = initPopulation()
    var mutationRate = 0.04f
    var mutationRange = 0.1f
    var eliteRate = 0.1f
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    val stats = AdaptableStats()

    /*
        Flexible Parameter from DNA
    */
    // Cache Variables
    val angleStepSize = Vec.TAU / pointsCount
    var allNormStructures = createAllNormStructures()

    var fitness = FloatArray(populationCount)

    fun createAllNormStructures(): Array<Structure> =
        Array(populationCount) { i -> createNormStructure(population[i]) }

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
//        g.image(target, 0f, 0f)

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

    fun mutate(dna: DNA): DNA {
        val newDNA = dna.copyOf()
        for (i in dna.indices) {
            if (Random.nextFloat() < mutationRate) {
                val new = Random.nextFloat()
                newDNA[i] = (1 - mutationRange) * dna[i] + mutationRange * new
            }
        }
        return newDNA
    }

    private fun initDNA(): DNA = FloatArray(sizeDNA) { Random.nextFloat() }

    private fun initPopulation(): Array<DNA> = Array(populationCount) { initDNA() }

    private fun createCrossoverDNA(): DNA {
        val i1 = fitness.selectOne()
        val i2 = fitness.selectOne()
        val crossoverDNA = population[i1].mix(population[i2], Random.nextInt(1, sizeDNA - 2))
        return mutate(crossoverDNA)
    }

    private fun createElitePopulation(count: Int): Array<DNA> {
        val eliteIndices = fitness.indexOfHighest(count)
        return Array(count) { i -> population[eliteIndices[i]] }
    }

    private fun createCrossoverPopulation(count: Int) = Array(count) { createCrossoverDNA() }

    private fun createPopulation(): Array<DNA> {
        val eliteCount = (populationCount * eliteRate).roundToInt().coerceAtLeast(1)
        val elite = createElitePopulation(eliteCount)
        val eliteMutation = Array(eliteCount) { i -> mutate(elite[i]) }
        return elite + eliteMutation + createCrossoverPopulation(populationCount - 2 * eliteCount)
    }

//    private fun createPopulation(): Array<DNA> = Array(populationCount) { i -> createDNA(i) }

    private fun evaluatePopulation() {
        stats.history.add(PopulationStats(fitness.max() ?: 0f, fitness.min() ?: 0f))
        stats.update()
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