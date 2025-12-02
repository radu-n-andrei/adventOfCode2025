package org.example.day02

fun main() {
    val ranges = LoadInput.load()
    println("P1:" + ranges.flatMap { r -> r.invalidNumbers(InvalidNumberBuilder::mirrorsForDigits) }.sum())
    println("P2:" + ranges.flatMap { r -> r.invalidNumbers(InvalidNumberBuilder::patternsForDigits) }.sum())
}