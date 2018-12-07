package com.exsilicium.aoc2018

import org.junit.Test
import kotlin.test.assertEquals

class Day06Test {
    private val sampleInput = """1, 1
    1, 6
    8, 3
    3, 4
    5, 5
    8, 9""".lineSequence().toCoordinates()

    @Test
    fun calculateGridBounds() {
        assertEquals(Pair(8, 9), com.exsilicium.aoc2018.calculateGridBounds(sampleInput))
    }
}