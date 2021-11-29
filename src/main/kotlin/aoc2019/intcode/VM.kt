package aoc2019.intcode

class VM {

    private var ip: Int = 0
    private val memory: IntArray

    constructor(program: IntArray) {
        memory = program.clone()
    }

    constructor(program: String) {
        memory = program.compile()
    }

    operator fun set(position: Int, value: Int) {
        memory[position] = value
    }

    operator fun get(position: Int) = memory[position]

    fun execute() {
        while (ip >= 0 && ip < memory.size && memory[ip] != 99) {
            step()
        }
    }

    @Suppress("ReplaceWithOperatorAssignment")
    fun step() = when (next) {
        1 -> param = param + param
        2 -> param = param * param
        99 -> {}
        else -> throw IllegalStateException("Unknown ${memory[ip - 1]} opcode at position ${ip - 1}")
    }

    private val next
        get() = memory[ip++]

    private var param: Int
        get() = memory[next]
        set(value) {
            memory[next] = value
        }

}

fun String.compile() =
    trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
