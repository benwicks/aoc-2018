package com.exsilicium.aoc2018

import com.exsilicium.aoc2018.puzzleinput.PuzzleInput

fun main(args: Array<String>) {
    println("Day 03 Pt. One Answer:\t" + Day03.calculateSquareInchesOfFabricWithinMultipleClaims(PuzzleInput.DAY_03))
    println("Day 03 Pt. Two Answer:\t" + Day03.findNonOverlappingClaim(PuzzleInput.DAY_03))
}

object Day03 {
    fun calculateSquareInchesOfFabricWithinMultipleClaims(input: Sequence<String>): Int {
        val squareInchesOfFabricWithinMultipleClaims = mutableSetOf<Point>()
        val claimsArea = mutableSetOf<Point>()
        input.map(::parseClaim)
            .flatMap { it.claimArea }
            .forEach {
                if (it in claimsArea) {
                    squareInchesOfFabricWithinMultipleClaims += it
                } else {
                    claimsArea += it
                }
            }
        return squareInchesOfFabricWithinMultipleClaims.size
    }

    private fun parseClaim(claimString: String): Claim {
        val parts = claimString.split("@")
        val id = parts[0].split("#")[1].trim().toInt()
        val remainingParts = parts[1].trim().split(":")
        val offsetParts = remainingParts[0].split(",")
        val sizeParts = remainingParts[1].split("x")

        return Claim(
            id,
            offsetParts[0].trim().toInt(),
            offsetParts[1].trim().toInt(),
            sizeParts[0].trim().toInt(),
            sizeParts[1].trim().toInt()
        )
    }

    fun findNonOverlappingClaim(input: Sequence<String>): Int {
        val nonOverlappingClaimIds = mutableSetOf<Int>()
        val claimsArea = mutableSetOf<ClaimPoint>()
        input.map(::parseClaim)
            .onEach { nonOverlappingClaimIds += it.id }
            .forEach { claim ->
                val overlappingClaimIds = getOverlappingClaimIds(claimsArea.toList(), claim.claimArea.toSet())
                if (overlappingClaimIds.isNotEmpty()) {
                    nonOverlappingClaimIds -= overlappingClaimIds
                    nonOverlappingClaimIds -= claim.id
                }
                claimsArea += claim.claimArea.map { point -> ClaimPoint(claim.id, point) }
            }
        return nonOverlappingClaimIds.first()
    }

    private fun getOverlappingClaimIds(claimPointsArea: List<ClaimPoint>, claimArea: Set<Point>): Set<Int> {
        val overlappingClaimIds = mutableSetOf<Int>()
        val claimsArea = claimPointsArea.map { it.point }
        claimArea.forEach {
            val indexOfPointInClaimPointsArea = claimsArea.indexOf(it)
            if (indexOfPointInClaimPointsArea >= 0) {
                overlappingClaimIds += claimPointsArea[indexOfPointInClaimPointsArea].claimId
            }
        }
        return overlappingClaimIds
    }
}

private data class Point(
    val x: Int,
    val y: Int
)

private data class Claim(
    val id: Int,
    val leftOffsetInches: Int,
    val topOffsetInches: Int,
    val widthInches: Int,
    val heightInches: Int
) {
    val claimArea: Sequence<Point> by lazy {
        val points = mutableListOf<Point>()
        (leftOffsetInches..(leftOffsetInches + widthInches - 1)).forEach { x ->
            (topOffsetInches..(topOffsetInches + heightInches - 1)).forEach { y ->
                points += Point(x, y)
            }
        }
        points.asSequence()
    }
}

private data class ClaimPoint(
    val claimId: Int,
    val point: Point
)