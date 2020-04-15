package de.ulrich_boeing.basics

import java.lang.RuntimeException

data class WeightedVal(val values: FloatArray, val weight: Float = 1f)

fun combine(vararg elements: WeightedVal): FloatArray {
    return elements.toList().getValues()
}

fun List<WeightedVal>.getValues(): FloatArray {
    val sumWeight = this.map{ it.weight }.sum()
    val numValues = this.getAndCheckNumValues()
    val fullValues = FloatArray(numValues) { i ->
        this.fold(0f) { sum, element -> sum + element.weight * element.values[i] }
    }
    return FloatArray(numValues) { i-> fullValues[i] / sumWeight }
}

fun List<WeightedVal>.getAndCheckNumValues(): Int {
    val numValues = this[0].values.size
    for (i in 1..this.lastIndex) {
        if (numValues != this[i].values.size)
            throw RuntimeException("values in WeightedValues don't have the same number of elements")
    }
    return numValues
}

fun FloatArray.withWeight(weight: Float = 1f) = WeightedVal(this, weight)


