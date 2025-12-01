package org.example.day01

data class Rotation(val direction: DialDirection, val value: Int, val overRotations: Int) {

    companion object {
        fun parseFile(fileName: String): List<Rotation> {
            return Rotation::class.java.getResourceAsStream(fileName).bufferedReader().readLines()
                .map { line ->
                    val d = DialDirection.valueOf(line.first().toString())
                    val nr = line.drop(1).toInt()
                    val value = nr % 100
                    val ov = nr / 100
                    Rotation(d, value, ov)
                }
        }
    }

    fun process(start: Int): Result {
        val end = when (direction) {
            DialDirection.L -> start - value
            DialDirection.R -> start + value
        }
        val normalized = if (end < 0) {
            99 + end + 1
        } else {
            if (end > 99) {
                end - 99 - 1
            } else {
                end
            }
        }
        val passedDueToNormalization =
            if (normalized != end && normalized != 0 && start != 0) 1 else 0

        return Result(normalized, passedDueToNormalization + overRotations)
    }
}