package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.*
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import java.lang.RuntimeException
import kotlin.math.roundToInt
import kotlin.random.Random

typealias DNA = FloatArray

open class Adaptable(val app: PApplet, val frame: Clipping) {
    open val sizeDNA: Int
        get() {
            throw RuntimeException("Please implement property sizeDNA [example: override val sizeDNA = 8]")
        }
    protected lateinit var population: Array<DNA>
    lateinit var fitness: FloatArray
    lateinit var optimizedFitness: FloatArray

    var mutationRate = 0.04f
    var mutationRange = 0.1f
    var eliteRate = 0.1f

    val stats = AdaptableStats()

    var populationCount = 0
        get() = field
        set(value) {
            field = value
            population = initPopulation(value)
            fitness = FloatArray(value)
        }

    private fun initDNA(): DNA = FloatArray(sizeDNA) { Random.nextFloat() }
    private fun initPopulation(count: Int): Array<DNA> = Array(count) { initDNA() }

    fun getBestFitness(): Int = fitness.indexOfMax()

    private fun mutate(dna: DNA): DNA {
        val newDNA = dna.copyOf()
        for (i in dna.indices) {
            if (Random.nextFloat() < mutationRate) {
                val new = Random.nextFloat()
                newDNA[i] = (1 - mutationRange) * dna[i] + mutationRange * new
            }
        }
        return newDNA
    }

    private fun createCrossoverPopulation(count: Int) = Array(count) { createCrossoverDNA() }

    private fun createCrossoverDNA(): DNA {
        val i1 = optimizedFitness.selectOne()
        val i2 = optimizedFitness.selectOne()
        val crossoverDNA = population[i1].mix(population[i2], Random.nextInt(1, sizeDNA - 2))
        return mutate(crossoverDNA)
    }

    private fun createElitePopulation(count: Int): Array<DNA> {
        val eliteIndices = fitness.indexOfHighest(count)
        return Array(count) { i -> population[eliteIndices[i]] }
    }

    fun optimizeFitness() {
        optimizedFitness = fitness.copyOf()
        optimizedFitness.norm()
    }

    fun createPopulation(): Array<DNA> {
        optimizeFitness()
        val eliteCount = (populationCount * eliteRate).roundToInt().coerceAtLeast(1)
        val elite = createElitePopulation(eliteCount)
        val eliteMutation = Array(eliteCount) { i -> mutate(elite[i]) }
        return elite + eliteMutation + createCrossoverPopulation(populationCount - 2 * eliteCount)
    }

    fun evaluatePopulation() {
        stats.history.add(PopulationStats(fitness.max() ?: 0f, fitness.min() ?: 0f))
        stats.update()
    }

    fun evaluateFitness(target: PImage, scale: Float) {
        for (i in population.indices) {
            val image = encloseDraw(population[i], scale)
            // TODO: image muss gleiche Grösse wie target haben, andere getDifference Methode wählen, alle Pixel summieren?
            val dif = target.getDifference(image)
            val colorValuesCount = image.width * image.height * 3 * 255
            fitness[i] = 1 - (dif / colorValuesCount.toFloat())
        }
    }

    fun sssgetImage(index: Int = 0, scale: Float = 1f): PImage {
        return encloseDraw(population[index], scale).copy()
    }

    fun encloseDraw(dna: DNA, scale: Float): PGraphics {
        val width = (frame.width * scale).roundToInt()
        val height = (frame.height * scale).roundToInt()
        val g = app.createGraphics(width, height)
        g.beginDraw()
        draw(dna, g)
        g.endDraw()
        return g
    }

    open fun draw(dna: DNA, g: PGraphics) {
        throw RuntimeException("Please implement function draw()")
    }

    fun nextGeneration(target: PImage, scale: Float = 1f) {
        evaluateFitness(target, scale)
        evaluatePopulation()
        population = createPopulation()
    }
}