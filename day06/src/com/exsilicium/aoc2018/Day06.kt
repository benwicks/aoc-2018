package com.exsilicium.aoc2018

import com.exsilicium.aoc2018.puzzleinput.PuzzleInput
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val coordinates = PuzzleInput.DAY_06.toCoordinates()
    println("Day 06 Pt. One Answer:\t" + calculateSizeOfLargestFiniteArea(coordinates))
    println("Day 06 Pt. Two Answer:\t" + calculateRegionSizeForAllPointsWithinGivenDistanceOfAll(coordinates, 10_000))
}

fun Sequence<String>.toCoordinates() = map { it.split(", ").let { part -> Pair(part[0].toInt(), part[1].toInt()) } }

fun calculateSizeOfLargestFiniteArea(coordinates: Sequence<Pair<Int, Int>>): Int {
    val maxPoint = calculateGridBounds(coordinates)

    return calculateClosestPoints(coordinates, maxPoint).filterValues {
        // Discard coordinates with infinite area.
        it.none { (x, y) -> x == 0 || y == 0 || x == maxPoint.first || y == maxPoint.second }
    }.maxBy { it.value.size }!!
        .value.size
}

fun calculateClosestPoints(
    coordinates: Sequence<Pair<Int, Int>>,
    maxPoint: Pair<Int, Int>
): Map<Pair<Int, Int>, Set<Pair<Int, Int>>> {
    return mutableMapOf<Pair<Int, Int>, Set<Pair<Int, Int>>>().apply {
        allPoints(maxPoint).forEach { point ->
            findClosestCoordinate(point, coordinates)?.let { closestCoordinate ->
                val newValue = if (closestCoordinate in this) {
                    get(closestCoordinate)!! + point
                } else {
                    setOf(point)
                }
                set(closestCoordinate, newValue)
            }
        }
    }.toMap()
}

fun findClosestCoordinate(point: Pair<Int, Int>, coordinates: Sequence<Pair<Int, Int>>) =
    coordinates.minBy { calculateManhattanDistance(it, point) }

fun allPoints(maxPoint: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val allPoints = mutableSetOf<Pair<Int, Int>>()
    for (x in 0..maxPoint.first) {
        for (y in 0..maxPoint.second) {
            allPoints += Pair(x, y)
        }
    }
    return allPoints.toSet()
}

fun calculateManhattanDistance(point1: Pair<Int, Int>, point2: Pair<Int, Int>) =
    (point1.first - point2.first).absoluteValue + (point1.second - point2.second).absoluteValue

fun calculateGridBounds(coordinates: Sequence<Pair<Int, Int>>): Pair<Int, Int> {
    var maxX = 0
    var maxY = 0
    coordinates.forEach { (x, y) ->
        if (x > maxX) {
            maxX = x
        }
        if (y > maxY) {
            maxY = y
        }
    }
    return Pair(maxX, maxY)
}

fun calculateRegionSizeForAllPointsWithinGivenDistanceOfAll(
    coordinates: Sequence<Pair<Int, Int>>,
    maxDistance: Int
): Int {
    return allPoints(calculateGridBounds(coordinates))
        .filter { point -> coordinates.sumBy { calculateManhattanDistance(it, point) } < maxDistance }
        .size
}
