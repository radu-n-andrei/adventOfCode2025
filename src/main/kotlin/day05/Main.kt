package org.example.day05

import org.example.LoadInput
import kotlin.math.max
import kotlin.math.min

data class Range(val low: Long, val high: Long) {
    companion object {
        fun fromString(range: String): Range {
            val parts = range.split("-")
            return Range(parts[0].toLong(), parts[1].toLong())
        }
    }

    fun overlapsWith(other: Range): Boolean {
        return other.low in low..high ||
                other.high in low..high ||
                high in other.low..other.high && low in other.low..other.high
    }

    fun merge(other: Range): Range {
        return Range(min(low, other.low), max(high, other.high))
    }

    fun size(): Long {
        return high - low + 1
    }
}

fun main() {
    val input = LoadInput.load("/day05/input.in") { br -> br.readLines() }
    val ranges = input.takeWhile(String::isNotEmpty).map(Range::fromString)
    val ids = input.takeLastWhile(String::isNotEmpty).map(String::toLong)
    println("P1: " + ids.filter { id -> ranges.find { range -> id in range.low..range.high } != null }.size)
    println("P2: " + mergeRanges(ranges, listOf()).sumOf(Range::size))

}

fun mergeRanges(rangesToCheck: List<Range>, sol: List<Range>): List<Range> {
    if (rangesToCheck.isEmpty()) {
        return sol
    } else {
        val toCheck = rangesToCheck.first()
        val rest = rangesToCheck.drop(1)
        val (overlapping, distinct) = rest.partition { r -> r.overlapsWith(toCheck) }
        if (overlapping.isNotEmpty()) {
            val merged = overlapping.fold(toCheck) { acc, r -> acc.merge(r) }
            return mergeRanges(distinct.plus(merged), sol)
        } else {
            return mergeRanges(distinct, sol.plus(toCheck))
        }


    }
}