package org.example.day03

import org.example.LoadInput
import kotlin.math.pow

fun main() {
    val input = LoadInput.load("/day03/input.in", { br -> br.readLines() })
    println("P1:" + input.sumOf { i -> joltage(i, 2) })
    println("P2:" + input.sumOf { i -> joltage(i, 12) })
}

data class Battery(val power: Int, val position: Int)

fun joltage(bank: String, batteryNumber: Int): Long {
    return dynamicJoltage(bank, batteryNumber, 0, batteryNumber - 1)
}

fun dynamicJoltage(bank: String, batteriesLeftToAdd: Int, totalPower: Long, atIndex: Int): Long {
    if (batteriesLeftToAdd == 0) {
        return totalPower
    } else {
        val batteries = bank.toList().zip((0..bank.length - 1))
            .map { p -> Battery(p.first.digitToInt(), p.second) }
        val currentBattery =
            batteries.dropLast(batteriesLeftToAdd - 1)
                .fold(
                    Battery(0, -1),
                    { acc: Battery, current: Battery -> if (current.power > acc.power) current else acc })
        return dynamicJoltage(
            bank.substring(currentBattery.position + 1),
            batteriesLeftToAdd - 1,
            totalPower + currentBattery.power * 10.0.pow(atIndex.toDouble()).toLong(),
            atIndex - 1
        )
    }

}