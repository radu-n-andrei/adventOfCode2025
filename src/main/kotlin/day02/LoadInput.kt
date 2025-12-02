package org.example.day02

object LoadInput {

    fun load(): List<Range> {
        return LoadInput::class.java.getResourceAsStream("/day02/input.in").bufferedReader().readLine().split(",")
            .map { line -> Range.fromString(line) }
    }
}