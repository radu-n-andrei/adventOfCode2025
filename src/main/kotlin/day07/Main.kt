package org.example.day07

import org.example.LoadInput
import org.example.Utils.zipWithIndex

data class Cell(val index: Int, val type: Char) {
    fun isStart(): Boolean = type == 'S'
    fun isSplitter(): Boolean = type == '^'
    fun splitsInto(max: Int): List<Int> =
        if (isSplitter()) {
            listOf(index - 1, index + 1).filter { it in 0..max }
        } else listOf(index)
}

fun main() {
    val input = LoadInput.load("/day07/input.in") { br ->
        br.readLines().map { s -> s.toCharArray().toList().zipWithIndex().map { p -> Cell(p.second, p.first) } }
    }
    val start = input.first().find(Cell::isStart)
    val beams = listOf(Pair(start!!.index, 1L))
    val expanded = expandBeams(input.drop(1).dropLast(1), beams, 0, 1)
    println("P1: " + expanded.first)
    println("P2: " + expanded.second)
}

fun expandBeams(
    cells: List<List<Cell>>,
    beams: List<Pair<Int, Long>>,
    splittersHit: Int,
    timelines: Long
): Pair<Int, Long> {
    if (cells.isEmpty()) return Pair(splittersHit, timelines)
    else {
        val currentCells = cells.first()
        val max = currentCells.size
        val nextBeams =
            beams.fold(Triple(listOf<Pair<Int, Long>>(), 0, 0L)) { next, i ->
                val currentCell = currentCells[i.first]

                if (currentCell.isSplitter()) {
                    val split = currentCell.splitsInto(max)
                    val nextBeams = next.first.plus(split.map { s -> Pair(s, i.second) }).groupBy { it.first }
                        .mapValues { j -> j.value.sumOf { it.second } }.toList()
                    // if one of the split beams falls outside the map, the number of timelines doesn't change
                    val adjustment = if (split.size == 2) 0 else -1
                    Triple(
                        nextBeams,
                        next.second + 1,
                        next.third + i.second + adjustment
                    )
                } else {
                    Triple(next.first.plus(i), next.second, next.third)
                }
            }
        return expandBeams(cells.drop(1), nextBeams.first, splittersHit + nextBeams.second, timelines + nextBeams.third)
    }
}
