package org.example.day08

import org.example.LoadInput
import kotlin.math.pow
import kotlin.math.sqrt

data class AdjacentJunction(val j1: Junction, val j2: Junction, val distance: Double) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is AdjacentJunction -> (j1 == other.j1 && j2 == other.j2) || (j1 == other.j2 && j2 == other.j1)
            else -> false
        }
    }

    fun contains(j: Junction): Boolean = j == j1 || j == j2

    override fun hashCode(): Int {
        var result = distance.hashCode()
        result = 31 * result + j1.hashCode()
        result = 31 * result + j2.hashCode()
        return result
    }
}

data class Junction(
    val x: Int,
    val y: Int,
    val z: Int,
    val adjacentJunctions: List<AdjacentJunction>,
) {
    companion object {
        fun parse(input: String): Junction {
            val coords = input.split(",").map(String::toInt)
            return Junction(coords[0], coords[1], coords[2], listOf())
        }
    }

    fun distanceTo(other: Junction): Double =
        sqrt((other.x - x).toDouble().pow(2.0) + (other.y - y).toDouble().pow(2.0) + (other.z - z).toDouble().pow(2.0))

    fun withNeighbours(others: List<Junction>): Junction {
        return copy(adjacentJunctions = others.map { j -> AdjacentJunction(this, j, distanceTo(j)) }
            .filterNot { it.distance == 0.0 }.sortedBy { it.distance })
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Junction -> {
                x == other.x && y == other.y && z == other.z
            }

            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        result = 31 * result + adjacentJunctions.hashCode()
        return result
    }
}

data class Circuit(val junctions: List<Junction>) {
    fun isConnectedTo(pair: AdjacentJunction): Boolean {
        return junctions.contains(pair.j1) || junctions.contains(pair.j2)
    }
}

fun main() {
    val input = LoadInput.load("/day08/input.in") { br -> br.readLines().map(Junction::parse) }
    val enhancedInput = input.map { it.withNeighbours(input) }
    println("P1: " + connect(enhancedInput, 1000, 3))
    println("P2: " + connectAll(enhancedInput))
}

fun connect(junctions: List<Junction>, times: Int, toCount: Int): Long {
    val adjacentJunctions = junctions.flatMap { it.adjacentJunctions }.sortedBy { it.distance }.take(times * 2)
    return adjacentJunctions.fold(listOf<Circuit>()) { acc, aj ->
        val existing = acc.filter { it.isConnectedTo(aj) }
        val adjustedAcc = acc.filterNot { existing.contains(it) }
        adjustedAcc.plus(Circuit(existing.flatMap { it.junctions }.plus(aj.j1).plus(aj.j2).distinct()))
    }.map { it.junctions.size.toLong() }.sortedWith { c1, c2 -> (c2 - c1).toInt() }.take(toCount)
        .reduce { acc1, acc2 -> acc1 * acc2 }
}

fun connectAll(junctions: List<Junction>): Long {
    val adjacentJunctions = junctions.flatMap { it.adjacentJunctions }.sortedBy { it.distance }
    val last = adjacentJunctions.fold(
        listOf<Circuit>()
    ) { acc, aj ->
        val existing = acc.filter { it.isConnectedTo(aj) }
        val adjustedAcc = acc.filterNot { existing.contains(it) }
        val newCircuit = Circuit(existing.flatMap { it.junctions }.plus(aj.j1).plus(aj.j2).distinct())
        // short circuit - break out of the fold
        if (newCircuit.junctions.size == junctions.size)
            return aj.j1.x.toLong() * aj.j2.x.toLong()
        adjustedAcc.plus(newCircuit)
    }
    return 0
}
