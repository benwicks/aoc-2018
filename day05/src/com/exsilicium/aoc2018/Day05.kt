package com.exsilicium.aoc2018

import com.exsilicium.aoc2018.puzzleinput.PuzzleInput

fun main(args: Array<String>) {
    println("Day 05 Pt. One Answer:\t" + countUnitsRemainingAfterFullReaction(PuzzleInput.DAY_05))
    println("Day 05 Pt. Two Answer:\t" +
        countUnitsRemainingAfterFullReaction(removeProblematicUnit(PuzzleInput.DAY_05)))
}

fun countUnitsRemainingAfterFullReaction(polymerInput: String): Int {
    var result = polymerInput
    do {
        val indexOfFirstReactingUnit = getIndexOfFirstReactingUnit(result)
        if (indexOfFirstReactingUnit >= 0) {
            result = result.removeRange(indexOfFirstReactingUnit..(indexOfFirstReactingUnit + 1))
        }
    } while (indexOfFirstReactingUnit >= 0)
    return result.length
}

fun getIndexOfFirstReactingUnit(polymer: String): Int {
    polymer.forEachIndexed { index, c ->
        if (index < polymer.length - 1) {
            val nextChar = polymer[index + 1]
            if (c.toLowerCase() == nextChar.toLowerCase() &&
                (c.isLowerCase() && nextChar.isUpperCase() || c.isUpperCase() && nextChar.isLowerCase())) {
                return index
            }
        }
    }
    return -1
}

fun removeProblematicUnit(input: String): String {
    return removeAllUnitsOfType(input, findProblematicUnit(input))
}

fun findProblematicUnit(input: String): Char {
    var mostProblematicUnit: Char? = null
    var longestLengthOfCollapsedPolymer = Integer.MAX_VALUE
    input.asSequence().map { it.toLowerCase() }.distinct()
        .forEach { unit ->
            val lengthOfCollapsedPolymer = countUnitsRemainingAfterFullReaction(removeAllUnitsOfType(input, unit))
            if (lengthOfCollapsedPolymer < longestLengthOfCollapsedPolymer) {
                longestLengthOfCollapsedPolymer = lengthOfCollapsedPolymer
                mostProblematicUnit = unit
            }
        }
    return mostProblematicUnit!!
}

fun removeAllUnitsOfType(input: String, unit: Char): String {
    val lowercaseUnit = unit.toLowerCase()
    return input.filter { c -> c.toLowerCase() != lowercaseUnit }
}
