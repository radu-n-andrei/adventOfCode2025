package org.example.day04

import org.example.LoadInput
import java.util.Optional

data class Row(val index: Int, val spaces: List<Boolean>)

fun main() {
    val input = LoadInput.load("/day04/input.in", { br -> br.readLines().map { line -> line.map { c -> c == '@' } } })
    val rows = input.zip(0..<input.size).map { (row, index) -> Row(index, row) }
    println("P1: " + movePaperAround(rows, false))
    println("P2: " + movePaperAround(rows, true))
}

fun movePaperAround(rows: List<Row>, keepGoing: Boolean): Int {
    if (!keepGoing) {
        return recCheck(rows, 0, 0, listOf()).first
    } else {
        fun moveSomeMore(rows: List<Row>, total: Int): Int {
            val (iterationTotal, newList) = recCheck(rows, 0, 0, listOf())
            if (iterationTotal != 0) {
                return moveSomeMore(newList, total + iterationTotal)
            } else {
                return total
            }
        }
        return moveSomeMore(rows, 0)
    }
}

fun recCheck(rows: List<Row>, currentRowIndex: Int, total: Int, updatedRows: List<Row>): Pair<Int, List<Row>> {
    val currentRow = rows[currentRowIndex]

    fun countAround(columnIndex: Int): Int {
        val neighbours = listOf(
            if (currentRow.index > 0) rows[currentRow.index - 1].spaces[columnIndex] else null,
            if (currentRow.index > 0 && columnIndex > 0) rows[currentRow.index - 1].spaces[columnIndex - 1] else null,
            if (currentRow.index > 0 && columnIndex < currentRow.spaces.size - 1) rows[currentRow.index - 1].spaces[columnIndex + 1] else null,
            if (columnIndex > 0) currentRow.spaces[columnIndex - 1] else null,
            if (columnIndex < currentRow.spaces.size - 1) currentRow.spaces[columnIndex + 1] else null,
            if (currentRow.index < rows.size - 1) rows[currentRow.index + 1].spaces[columnIndex] else null,
            if (currentRow.index < rows.size - 1 && columnIndex > 0) rows[currentRow.index + 1].spaces[columnIndex - 1] else null,
            if (currentRow.index < rows.size - 1 && columnIndex < currentRow.spaces.size - 1) rows[currentRow.index + 1].spaces[columnIndex + 1] else null,
        )
        return neighbours.filter { b -> b != null && b }.size
    }

    val valid = currentRow.spaces.zip(0..<currentRow.spaces.size).filter { (space, index) ->
        space && countAround(index) < 4
    }
    val validSize = valid.size
    val updatedRow = currentRow.spaces.zip(0..<currentRow.spaces.size).map { (s, i) ->
        if (valid.map { p -> p.second }.contains(i)) false else s
    }
    val newRows = updatedRows.plus(currentRow.copy(spaces = updatedRow))

    return if (currentRowIndex < rows.size - 1) recCheck(
        rows,
        currentRowIndex + 1,
        total + validSize,
        newRows
    ) else Pair(total + validSize, newRows)
}

