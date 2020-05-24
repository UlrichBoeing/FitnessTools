package de.ulrich_boeing.basics

class Mean {
    private var sum = 0f
    private var sumWeight = 0f

    fun add(value: Float, weight: Float = 1f) {
        sum += value * weight
        sumWeight += weight
    }

    fun get(): Float = if (sumWeight !=  0f) sum / sumWeight else 0f
}