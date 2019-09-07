package de.ulrich_boeing.basics

import kotlin.math.roundToInt

fun lerp(value: Float, other: Float, amount: Float) : Float =  (1 - amount) * value + amount * other

fun lerp(value: Int, other: Int, amount: Float): Int = lerp(value.toFloat(), other.toFloat(), amount).roundToInt()