package com.exsilicium.aoc2018

import com.exsilicium.aoc2018.puzzleinput.PuzzleInput
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    println("Day 04 Pt. One Answer:\t" + calculateGuardAndMinuteByStrategyOne(PuzzleInput.DAY_04).pairAsProduct())
    println("Day 04 Pt. Two Answer:\t" + calculateGuardAndMinuteByStrategyTwo(PuzzleInput.DAY_04).pairAsProduct())
}

private fun Pair<Int, Int>.pairAsProduct() = "$first * $second = ${first * second}"

/**
 * Find the guard that has the most minutes asleep. What minute does that guard spend asleep the most?
 */
private fun calculateGuardAndMinuteByStrategyOne(input: Sequence<String>): Pair<Int, Int> {
    val guardWhoSleptLongest = calculateGuardIdsToSleepRanges(input).maxBy { guardSleepRanges ->
        guardSleepRanges.value.sumBy { sleepRanges ->
            sleepRanges.count()
        }
    }!!

    return Pair(guardWhoSleptLongest.key, minuteMostLikelyToBeAsleepAndFrequency(guardWhoSleptLongest.value).first)
}

/**
 * Of all guards, which guard is most frequently asleep on the same minute?
 */
private fun calculateGuardAndMinuteByStrategyTwo(input: Sequence<String>): Pair<Int, Int> {
    var mostFrequentlyAsleepOnAGivenMinuteCount = 0
    lateinit var guardAndMostFrequentlyAsleepMinute: Pair<Int, Int>

    calculateGuardIdsToSleepRanges(input).forEach { guardId, sleepRanges ->
        val minuteMostLikelyToBeAsleepAndFrequency = minuteMostLikelyToBeAsleepAndFrequency(sleepRanges)
        val maximumFrequency = minuteMostLikelyToBeAsleepAndFrequency.second
        if (mostFrequentlyAsleepOnAGivenMinuteCount < maximumFrequency) {
            mostFrequentlyAsleepOnAGivenMinuteCount = maximumFrequency
            guardAndMostFrequentlyAsleepMinute = Pair(guardId, minuteMostLikelyToBeAsleepAndFrequency.first)
        }
    }

    return guardAndMostFrequentlyAsleepMinute
}

private fun minuteMostLikelyToBeAsleepAndFrequency(sleepRanges: List<IntRange>): Pair<Int, Int> {
    val minuteFrequencies = IntArray(60) { 0 }
    sleepRanges.forEach { sleepRange ->
        sleepRange.forEach { minuteInRange ->
            minuteFrequencies[minuteInRange]++
        }
    }
    return Pair(minuteFrequencies.indexOf(minuteFrequencies.max()!!), minuteFrequencies.max()!!)
}

private fun calculateGuardIdsToSleepRanges(input: Sequence<String>): MutableMap<Int, List<IntRange>> {
    val guardIdsToSleepRanges = mutableMapOf<Int, List<IntRange>>()

    parseAndSortRecords(input).forEach { record ->
        if (record.guardId in guardIdsToSleepRanges) {
            val newSleepRanges = guardIdsToSleepRanges[record.guardId]!!.toMutableList() + record.shiftSleepRanges
            guardIdsToSleepRanges.replace(record.guardId, newSleepRanges)
        } else {
            guardIdsToSleepRanges[record.guardId] = record.shiftSleepRanges
        }
    }

    return guardIdsToSleepRanges
}

private fun parseAndSortRecords(input: Sequence<String>): Set<GuardDutyLog> {
    val shiftStarts = mutableSetOf<Pair<Int, LocalDateTime>>()
    val events = mutableSetOf<Pair<LocalDateTime, Boolean>>()

    input.forEach { row ->
        if (" Guard #" in row) {
            val parts = row.split(" Guard #")
            val guardId = parts[1].split(" begins shift")[0].toInt()
            val shiftStartDateTime = LocalDateTime.parse(parts[0].removeSurrounding("[", "]"), dateTimeFormatter)
            shiftStarts += Pair(guardId, shiftStartDateTime)
        } else {
            val parts = row.split("] ")
            val dateTime = LocalDateTime.parse(parts[0].removePrefix("["), dateTimeFormatter)
            val isAsleep = "falls asleep" == parts[1]
            events += Pair(dateTime, isAsleep)
        }
    }

    val sortedEvents = events.asSequence().sortedBy { it.first }

    return shiftStarts.asSequence().sortedBy { it.second }
        .map { shiftStart ->
            val shiftStartTime = shiftStart.second
            val dayOfShift = if (shiftStartTime.hour == 0) {
                shiftStartTime.toLocalDate()
            } else {
                shiftStartTime.toLocalDate().plusDays(1)
            }
            val eventsOnShift = sortedEvents.filter { event -> event.first.toLocalDate() == dayOfShift }
                .map { GuardDutyEvent(it.first.minute, it.second) }
                .toSet()
            GuardDutyLog(shiftStart.first, shiftStartTime, eventsOnShift)
        }.toSet()
}

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

private data class GuardDutyLog(
    val guardId: Int,
    val dateShiftStarted: LocalDateTime,
    private val events: Set<GuardDutyEvent>
) {
    val shiftSleepRanges = events.chunked(2) { events -> events[0].minute..events[1].minute }
}

private data class GuardDutyEvent(
    val minute: Int,
    val isAsleep: Boolean
)
