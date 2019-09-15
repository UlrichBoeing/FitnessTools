package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.*
import processing.core.PApplet
import java.lang.RuntimeException
import kotlin.math.roundToInt
import kotlin.random.Random

typealias DNA = FloatArray

open class Adaptable(val app: PApplet, val frame: Clipping)  {
    open val sizeDNA : Int
        get() {
            throw RuntimeException("Please implement property sizeDNA [example: override val sizeDNA = 8]")
        }
    protected  lateinit var population : Array<DNA>
    lateinit var fitness : FloatArray

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

//    open fun getDNASize(): Int {
//        throw RuntimeException("Please implement setDNASize()")
//    }

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
        val i1 = fitness.selectOne()
        val i2 = fitness.selectOne()
        val crossoverDNA = population[i1].mix(population[i2], Random.nextInt(1, sizeDNA - 2))
        return mutate(crossoverDNA)
    }
    private fun createElitePopulation(count: Int): Array<DNA> {
        val eliteIndices = fitness.indexOfHighest(count)
        return Array(count) { i -> population[eliteIndices[i]] }
    }

    fun createPopulation(): Array<DNA> {
        val eliteCount = (populationCount * eliteRate).roundToInt().coerceAtLeast(1)
        val elite = createElitePopulation(eliteCount)
        val eliteMutation = Array(eliteCount) { i -> mutate(elite[i]) }
        return elite + eliteMutation + createCrossoverPopulation(populationCount - 2 * eliteCount)
    }

    fun evaluatePopulation() {
        stats.history.add(PopulationStats(fitness.max() ?: 0f, fitness.min() ?: 0f))
        stats.update()
    }

}