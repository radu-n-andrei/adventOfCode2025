package org.example

object Utils {
    fun <T> List<T>.zipWithIndex(): List<Pair<T, Int>> =
        zip(indices)
}