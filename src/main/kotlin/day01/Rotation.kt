package org.example.day01

data class Result(val end: Int, val zeros: Int)
enum class DialDirection {
    L, R
}

data class Rotation(val direction: DialDirection, val value: Int, val overRotations: Int) {

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