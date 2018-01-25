package com.patres.pointtype.utils

import processing.core.PVector
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class RandomGenerator {
    companion object {
        var random = Random()

        fun generateFloat(min: Float = 0f, max: Float): Float = random.nextFloat() * (max - min) + min
        fun generateDouble(min: Double = 0.0, max: Double): Double = random.nextDouble() * (max - min) + min
        fun generateVector(minX: Float = 0f, maxX: Float, minY: Float = 0f, maxY: Float): PVector = PVector(generateFloat(minX, maxX), generateFloat(minY, maxY))
        fun generateBooleanNumber(): Int = if (random.nextBoolean()) 1 else -1
    }
}

