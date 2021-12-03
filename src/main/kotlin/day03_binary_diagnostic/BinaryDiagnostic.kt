package day03_binary_diagnostic

fun main() {
    util.solve(3687446, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Long {
    val freqs = IntArray(input.lineSequence().first().length)
    for (line in input.lineSequence()) {
        line.forEachIndexed { i, c ->
            freqs[i] += if (c == '1') 1 else -1
        }
    }
    var gamma = 0L
    var epsilon = 0L
    freqs.forEach { c ->
        gamma = gamma shl 1
        epsilon = epsilon shl 1
        if (c > 0) ++gamma else ++epsilon
    }
    return gamma * epsilon
}


fun partTwo(input: String) = input.trim().length
