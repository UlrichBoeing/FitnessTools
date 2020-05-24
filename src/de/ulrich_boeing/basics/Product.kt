package de.ulrich_boeing.basics

import kotlin.math.pow

class Product() {
    private var product: Float = 1f
    private var weightSum = 0f

    fun add(value: Float, weight: Float = 1f) {
        val weightValue = (1 - weight) + value * weight
        product *= weightValue
        weightSum += weight
    }

    fun get(): Float = if (weightSum == 0f) 1f else product// .pow(1f / weightSum)
}