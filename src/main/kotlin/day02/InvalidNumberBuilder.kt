package org.example.day02

import kotlin.math.pow

/**
 * Build and cache different types of invalid numbers by number of digits
 */
object InvalidNumberBuilder {

    private val mirrorMemo: MutableMap<Int, List<Long>> = mutableMapOf()
    private val patternsMemo: MutableMap<Int, List<Long>> = mutableMapOf()

    fun mirrorsForDigits(n: Int): List<Long> {
        if (mirrorMemo.containsKey(n)) {
            return mirrorMemo.getValue(n)
        } else {
            if (n <= 0 || n % 2 == 1) {
                mirrorMemo[n] = listOf()
                return listOf()
            } else {
                val half = n / 2
                val start = 10.0.pow(half.toDouble() - 1).toLong()
                val end = 10.0.pow(half.toDouble()).toLong() - 1
                val invalidNumbers = (start..end).toList().map { i -> (i.toString() + i.toString()).toLong() }
                mirrorMemo[n] = invalidNumbers
                return invalidNumbers
            }
        }
    }

    fun patternsForDigits(n: Int): List<Long> {
        if (patternsMemo.containsKey(n)) {
            return patternsMemo.getValue(n)
        } else {
            val groupSizes = (1..n / 2).filter { i -> n % i == 0 }
            val invalidNumbers = groupSizes.flatMap { gSize ->
                val start = 10.0.pow(gSize.toDouble() - 1).toLong()
                val end = 10.0.pow(gSize.toDouble()).toLong() - 1
                (start..end).toList().map { i -> i.toString().repeat(n / gSize).toLong() }
            }.distinct()
            patternsMemo[n] = invalidNumbers
            return invalidNumbers
        }
    }


}