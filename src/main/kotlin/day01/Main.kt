package org.example.day01

import org.example.LoadInput

fun main() {
    //val rotations = Rotation.parseFile("/day01/input.in")
    val rotations = LoadInput.load("/day01/input.in") { br ->
        br.readLines()
            .map { line ->
                val d = DialDirection.valueOf(line.first().toString())
                val nr = line.drop(1).toInt()
                val value = nr % 100
                val ov = nr / 100
                Rotation(d, value, ov)
            }
    }

    println(
        "P1: " +
                rotations.fold(initial = Result(50, 0), operation = { current: Result, rotation: Rotation ->
                    val newCurrent = rotation.process(current.end)
                    val updatedZeros = if (newCurrent.end == 0) current.zeros + 1 else current.zeros
                    newCurrent.copy(zeros = updatedZeros)
                }).zeros
    )

    println(
        "P2: " +
                rotations.fold(initial = Result(50, 0), operation = { current: Result, rotation: Rotation ->
                    val newCurrent = rotation.process(current.end)
                    val updatedZeros = if (newCurrent.end == 0) current.zeros + 1 else current.zeros
                    newCurrent.copy(zeros = newCurrent.zeros + updatedZeros)
                }).zeros
    )
}