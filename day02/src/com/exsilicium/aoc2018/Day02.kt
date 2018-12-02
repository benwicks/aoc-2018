package com.exsilicium.aoc2018

import com.exsilicium.aoc2018.puzzleinput.PuzzleInput

fun main(args: Array<String>) {
    println("Day 02 Pt. One Answer:\t" + Day02.calculateChecksumOfBoxIds(PuzzleInput.DAY_02.toList()))
    println("Day 02 Pt. Two Answer:\t" + Day02.getCommonLettersBetweenTwoCorrectBoxIds(PuzzleInput.DAY_02))
}

internal object Day02 {
    fun calculateChecksumOfBoxIds(boxIds: List<String>): Int {
        var numBoxIdsWithExactlyTwoOfAnyLetter = 0
        var numBoxIdsWithExactlyThreeOfAnyLetter = 0

        boxIds.map { it.toCharArray() }.forEach {
            if (containsExactlyCountOfAnyLetter(it, 2)) {
                numBoxIdsWithExactlyTwoOfAnyLetter++
            }
            if (containsExactlyCountOfAnyLetter(it, 3)) {
                numBoxIdsWithExactlyThreeOfAnyLetter++
            }
        }

        return numBoxIdsWithExactlyTwoOfAnyLetter * numBoxIdsWithExactlyThreeOfAnyLetter
    }

    private fun containsExactlyCountOfAnyLetter(boxId: CharArray, count: Int): Boolean {
        return boxId.firstOrNull { character -> (boxId.count { it == character } == count) } != null
    }

    fun getCommonLettersBetweenTwoCorrectBoxIds(boxIds: Sequence<String>): String {
        val checkedBoxIds = mutableSetOf<String>()

        boxIds.forEach { boxId1 ->
            checkedBoxIds += boxId1

            (boxIds - checkedBoxIds).mapNotNull { indexOfSingleCharacterThatDiffers(boxId1, it) }
                .firstOrNull()?.let {
                    return boxId1.removeRange(it, it + 1)
                }
        }

        throw NoSuchElementException("Could not find 2 correct Box IDs in given input.")
    }

    private fun indexOfSingleCharacterThatDiffers(boxId1: String, boxId2: String): Int? {
        var indexOfFirstCharacterThatDiffers: Int? = null
        var numCharactersThatDiffer = 0
        boxId1.forEachIndexed { index, char ->
            if (char != boxId2[index]) {
                if (numCharactersThatDiffer > 0) {
                    return null
                } else {
                    numCharactersThatDiffer++
                    indexOfFirstCharacterThatDiffers = index
                }
            }
        }
        return indexOfFirstCharacterThatDiffers
    }
}