package com.exsilicium.aoc2018

import org.junit.Test
import kotlin.test.assertEquals

internal class Day01Test {

    @Test
    fun sumFrequenciesInList() {
        assertEquals(9, Day01.sumFrequenciesInList(sequenceOf(1, 3, 5)))
    }
}