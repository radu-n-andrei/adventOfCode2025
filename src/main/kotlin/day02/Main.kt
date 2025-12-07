package org.example.day02

import org.example.LoadInput

fun main() {
    val ranges = LoadInput.load("/day02/input.in") { br ->
        br.readLine().split(",")
            .map { line -> Range.fromString(line) }
    }
    println("P1:" + ranges.flatMap { r -> r.invalidNumbers(InvalidNumberBuilder::mirrorsForDigits) }.sum())
    println("P2:" + ranges.flatMap { r -> r.invalidNumbers(InvalidNumberBuilder::patternsForDigits) }.sum())
}