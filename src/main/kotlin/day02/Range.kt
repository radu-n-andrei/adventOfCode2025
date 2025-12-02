package org.example.day02

data class Range(val begin: Long, val end: Long) {

    companion object {
        fun fromString(rangeStr: String): Range {
            val (beginStr, endStr) = rangeStr.split("-")
            return Range(beginStr.toLong(), endStr.toLong())
        }
    }

    fun invalidNumbers(numberGenerator: (Int) -> List<Long>): List<Long> {
        return (begin.toString().length..end.toString().length).toList()
            .flatMap(numberGenerator)
            .filter { i -> i in begin..end }
    }

}
