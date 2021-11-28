package aoc2019.day02_program_alarm

fun main() {
    util.solve(4090689, ::partOne)
    util.solve(::partTwo)
}

private fun String.compile() =
    trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()

fun partOne(input: String): Int {
    val codes = input.compile()
    codes[1] = 12
    codes[2] = 2
    return evaluateForPosition(codes, 0)
}

internal fun evaluateForPosition(program: String, position: Int) =
    evaluateForPosition(program.compile(), position)

internal fun evaluateForPosition(program: IntArray, position: Int): Int {
    var ip = 0
    while (true) {
        // short-circuit HALT
        if (program[ip] == 99) {
            return program[position]
        }
        val a = program[program[ip + 1]]
        val b = program[program[ip + 2]]
        val destIdx = program[ip + 3]
        when (program[ip]) {
            1 -> { // ADD
                program[destIdx] = a + b
            }
            2 -> { // MULT
                program[destIdx] = a * b
            }
            else -> throw IllegalStateException("Unknown ${program[ip]} opcode at position $ip")
        }
        ip += 4
    }
}

fun partTwo(input: String) = input.trim().length
