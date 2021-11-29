package aoc2019.day02_program_alarm

import aoc2019.intcode.VM
import aoc2019.intcode.compile

fun main() {
    util.solve(4090689, ::partOne)
    util.solve(7733, ::partTwo)
}

fun partOne(input: String): Int {
    val codes = input.compile()
    codes[1] = 12
    codes[2] = 2
    return evaluateForPosition(codes, 0)
}

internal fun evaluateForPosition(program: String, position: Int) =
    evaluateForPosition(program.compile(), position)

internal fun evaluateForPosition(program: IntArray, position: Int): Int {
    val vm = VM(program)
    vm.execute()
    return vm[position]
}

fun partTwo(input: String): Int {
    val codes = input.compile()
    for (noun in 0..99) {
        for (verb in 0..99) {
            val vm = VM(codes)
            vm[1] = noun
            vm[2] = verb
            vm.execute()
            if (vm[0] == 19690720) {
                return noun * 100 + verb
            }
        }
    }
    throw IllegalStateException("No noun:verb pair found?!")
}
