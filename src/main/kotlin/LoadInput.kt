package org.example

import java.io.BufferedReader

object LoadInput {
    fun <T> load(fileName: String, transform: (BufferedReader) -> List<T>): List<T> {
        return transform(LoadInput::class.java.getResourceAsStream(fileName).bufferedReader())
    }
}