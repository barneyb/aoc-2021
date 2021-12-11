package day10_syntax_scoring

import java.util.*

fun main() {
    util.solve(290691, ::partOne)
    util.solve(::partTwo)
}

private val SCORE_LEGAL = 0
private val SCORE_INCOMPLETE = -1
private val SCORE_PAREN = 3
private val SCORE_BRACKET = 57
private val SCORE_BRACE = 1197
private val SCORE_ANGLE = 25137

fun partOne(input: String) =
    input
        .lineSequence()
        .map(String::toSyntaxScore)
        .filter { it > 0 }
        .sum()

private fun String.toSyntaxScore(): Int {
    val stack: Deque<Char> = LinkedList()
    forEach { c ->
        when (c) {
            '(' -> stack.addFirst(')')
            '[' -> stack.addFirst(']')
            '{' -> stack.addFirst('}')
            '<' -> stack.addFirst('>')
            ')' -> if (stack.removeFirst() != ')') return SCORE_PAREN
            ']' -> if (stack.removeFirst() != ']') return SCORE_BRACKET
            '}' -> if (stack.removeFirst() != '}') return SCORE_BRACE
            '>' -> if (stack.removeFirst() != '>') return SCORE_ANGLE
            else -> throw IllegalArgumentException("Unknown char '$c'")
        }
    }
    return if (stack.isEmpty())
        SCORE_LEGAL
    else
        SCORE_INCOMPLETE
}

fun partTwo(input: String) = input.trim().length
