package com.example.smartdialer

import java.util.*

object T9Utils {
    private val map = mapOf(
        '2' to "abc", '3' to "def", '4' to "ghi",
        '5' to "jkl", '6' to "mno", '7' to "pqrs",
        '8' to "tuv", '9' to "wxyz"
    )

    // convert name to T9 digit string; non-mapped characters -> space
    fun nameToT9(name: String): String {
        val sb = StringBuilder()
        val lower = name.lowercase(Locale.getDefault())
        for (ch in lower) {
            val entry = map.entries.firstOrNull { it.value.contains(ch) }
            sb.append(entry?.key ?: ' ')
        }
        return sb.toString().trim()
    }

    // returns true if digits match start of name's T9 encoding
    fun matchesT9(digits: String, name: String): Boolean {
        if (digits.isEmpty()) return false
        val t9 = nameToT9(name)
        return t9.startsWith(digits)
    }
}
