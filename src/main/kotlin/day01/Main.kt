package org.example.day01

fun main() {
    val rotations = Rotation.parseFile("/day01/input.in")
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