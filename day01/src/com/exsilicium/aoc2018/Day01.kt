package com.exsilicium.aoc2018

import com.exsilicium.aoc2018.puzzleinput.PuzzleInput

internal fun main(args: Array<String>) {
    println("Day 01 Pt. One Answer:\t" + Day01.sumFrequenciesInList(PuzzleInput.DAY_01))
    println("Day 01 Pt. Two Answer:\t" + Day01.findFirstDuplicateFrequency(PuzzleInput.DAY_01))
}

internal object Day01 {
    fun sumFrequenciesInList(input: Sequence<Int>) = input.sum()

    fun findFirstDuplicateFrequency(input: Sequence<Int>): Int {
        val calculatedFrequencies = mutableListOf<Int>()
        var index = 0
        var frequency = 0

        val inputList = input.toList()

        do {
            frequency += inputList[index]
            calculatedFrequencies += frequency
            index = (index + 1) % inputList.size
        } while (frequency !in calculatedFrequencies.dropLast(1))
        return frequency
    }
}