package day10_syntax_scoring

import java.util.*

fun main() {
    util.solve(290691, ::partOne)
    // 240.165μs initially
    // 243.996μs with "pattern" types
    util.solve(2768166558, ::partTwo)
    // 213.655μs initially
    // 199.046μs with "pattern" types
}

fun partOne(input: String) =
    input
        .lineSequence()
        .map(String::toSyntaxScore)
        .sumOf {
            when (it) {
                is Corrupt -> it.score
                else -> 0
            }
        }

private interface Score
private class Corrupt(val score: Long) : Score
private class Incomplete(val score: Long) : Score
private class Legal : Score

private fun String.toSyntaxScore(): Score {
    val stack: Deque<Char> = ArrayDeque()
    forEach {
        when (it) {
            '(' -> stack.addFirst(')')
            '[' -> stack.addFirst(']')
            '{' -> stack.addFirst('}')
            '<' -> stack.addFirst('>')
            ')' -> if (stack.removeFirst() != ')')
                return Corrupt(3)
            ']' -> if (stack.removeFirst() != ']')
                return Corrupt(57)
            '}' -> if (stack.removeFirst() != '}')
                return Corrupt(1197)
            '>' -> if (stack.removeFirst() != '>')
                return Corrupt(25137)
            else -> throw IllegalArgumentException("Unknown char '$it'")
        }
    }
    if (stack.isEmpty()) return Legal()
    return Incomplete(stack.map {
        when (it) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> throw IllegalArgumentException("Unknown stacked char '$it'")
        }
    }
        .fold(0L) { sum, n -> sum * 5 + n })
}

fun partTwo(input: String) =
    input
        .lineSequence()
        .map(String::toSyntaxScore)
        .map {
            when (it) {
                is Incomplete -> it.score
                else -> -1
            }
        }
        .filter { it > 0 }
        .sorted()
        .toList()
        .let {
            it[it.size / 2]
        }
