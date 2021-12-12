package day10_syntax_scoring

import java.util.*

fun main() {
    util.solve(290691, ::partOne)
    util.solve(2768166558, ::partTwo)
}

private val SCORE_LEGAL: Long = 0
private val SCORE_PAREN: Long = 3
private val SCORE_BRACKET: Long = 57
private val SCORE_BRACE: Long = 1197
private val SCORE_ANGLE: Long = 25137

private val COMP_PAREN: Long = 1
private val COMP_BRACKET: Long = 2
private val COMP_BRACE: Long = 3
private val COMP_ANGLE: Long = 4

fun partOne(input: String) =
    input
        .lineSequence()
        .map(String::toSyntaxScore)
        .filter { it > 0 }
        .sum()

private fun String.toSyntaxScore(): Long {
    val stack: Deque<Char> = ArrayDeque()
    forEach {
        when (it) {
            '(' -> stack.addFirst(')')
            '[' -> stack.addFirst(']')
            '{' -> stack.addFirst('}')
            '<' -> stack.addFirst('>')
            ')' -> if (stack.removeFirst() != ')') return SCORE_PAREN
            ']' -> if (stack.removeFirst() != ']') return SCORE_BRACKET
            '}' -> if (stack.removeFirst() != '}') return SCORE_BRACE
            '>' -> if (stack.removeFirst() != '>') return SCORE_ANGLE
            else -> throw IllegalArgumentException("Unknown char '$it'")
        }
    }
    return if (stack.isEmpty())
        SCORE_LEGAL
    else {
        -stack.map {
            when (it) {
                ')' -> COMP_PAREN
                ']' -> COMP_BRACKET
                '}' -> COMP_BRACE
                '>' -> COMP_ANGLE
                else -> throw IllegalArgumentException("Unknown stacked char '$it'")
            }
        }
            .fold(0L) { sum, n -> sum * 5 + n }
    }
}

fun partTwo(input: String) =
    -input
        .lineSequence()
        .map(String::toSyntaxScore)
        .filter { it < 0 }
        .sorted()
        .toList()
        .let {
            it[it.size / 2]
        }
