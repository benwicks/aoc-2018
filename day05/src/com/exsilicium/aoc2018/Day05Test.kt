package com.exsilicium.aoc2018

import org.junit.Test
import kotlin.test.assertEquals

internal class Day05Test {
    @Test
    fun countUnitsRemainingAfterFullReaction() {
        assertEquals(10, countUnitsRemainingAfterFullReaction("dabAcCaCBAcCcaDA"))
    }

    @Test
    fun getIndexOfFirstReactingUnit() {
        assertEquals(4, getIndexOfFirstReactingUnit("dabAcCaCBAcCcaDA"))
    }

    @Test
    fun removeProblematicUnit() {
        assertEquals("dabAaBAaDA", removeProblematicUnit("dabAcCaCBAcCcaDA"))
    }

    @Test
    fun findProblematicUnit() {
        assertEquals('c', findProblematicUnit("dabAcCaCBAcCcaDA"))
    }

    @Test
    fun removeAllUnitsOfType() {
        assertEquals("dabAaBAaDA", removeAllUnitsOfType("dabAcCaCBAcCcaDA", 'c'))
    }
}