package day10_syntax_scoring

import java.util.*

/**
 * "Chunks contain ... other chunks" means recursion is the name of the game,
 * and that means a stack. Some lookup tables for matching delimiters and scores
 * will be needed too. Since we're again interpreting the input (not treating it
 * as data), this will be a parser-heavy task.
 *
 * Turns out part one also partitioned the input lines into corrupt and
 * incomplete halves (you noticed "all of them" for which lines had errors,
 * right?). Now instead of using the stacks to find mismatches, we need to use
 * what is left on the stacks to compute a score. Comprehending a collection is
 * a cinch. Phew.
 */
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
