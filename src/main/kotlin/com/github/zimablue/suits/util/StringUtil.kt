package com.github.zimablue.suits.util

object StringUtil {
    fun MutableList<String>.deleteLine() : MutableList<String> {
        return this.apply { removeIf { it.contains("DELETE") } }
    }
    fun List<String>.joinLine() : String {
        return this.joinToString("<Ln>")
    }
    fun String.splitLine() : List<String> {
        return this.split("<Ln>")
    }
}