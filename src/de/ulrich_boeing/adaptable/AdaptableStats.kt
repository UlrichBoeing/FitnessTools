package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.HistoryList

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

