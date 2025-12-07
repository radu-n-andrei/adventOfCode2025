package org.example.day06

import org.example.LoadInput
import org.example.Utils.zipWithIndex

data class Operation(val isAddition: Boolean, val operands: List<Long>) {
    fun performOperation(): Long {
        return if (isAddition) {
            operands.sum()
        } else operands.reduce { a, b -> a * b }
    }
}

data class FunkyOperation(val isAddition: Boolean, val operands: List<String>) {
    fun performOperation(): Long {
        val degree = operands.first().length
        val revd = operands.map { it.reversed() }
        val ops = (0..<degree).map { i -> revd.map { it[i] }.filter { it != ' ' }.toList().joinToString("") }
            .map(String::toLong)
        return Operation(isAddition, ops).performOperation()
    }

    fun lengthNormalized(): FunkyOperation {
        val maxLen = operands.maxOf { it.length }
        return copy(operands = operands.map { it.padEnd(maxLen, ' ') })
    }
}

fun main() {
    val rawInput = LoadInput.load("/day06/input.in") { br -> br.readLines() }
    println("P1: ${p1(rawInput)}")
    println("P2: ${p2(rawInput)}")
}

fun p1(raw: List<String>): Long {
    val inputs = raw.map { it.trim().replace("\\s+".toRegex(), " ") }
    val operands = inputs.dropLast(1).map { s -> s.split(" ").map(String::toLong) }
    val operators = inputs.last().split(" ")
    val startingOperations = operands.first().indices.map {
        Operation(true, listOf())
    }
    val operationsWithOperands = operands.fold(startingOperations) { acc, ops ->
        acc.zip(ops).map { (a, b) -> a.copy(operands = a.operands.plus(b)) }
    }
    val fullOperations = operators.zip(operationsWithOperands).map { (operator, operation) ->
        operation.copy(isAddition = operator == "+")
    }
    return fullOperations.sumOf(Operation::performOperation)
}

fun p2(raw: List<String>): Long {
    val operands = raw.dropLast(1)
    val operators = raw.last().toCharArray().toList()
    val prepedOperators = operators.zipWithIndex().filter { it.first != ' ' }

    val funkyOperations = prepedOperators.zipWithNext().map { (current, next) ->
        FunkyOperation(
            current.first == '+',
            operands.map { it.substring(current.second, next.second - 1) }
        )
    }.plus(
        FunkyOperation(
            prepedOperators.last().first == '+',
            operands.map { it.substring(prepedOperators.last().second) }).lengthNormalized()
    )
    return funkyOperations.sumOf(FunkyOperation::performOperation)
}

