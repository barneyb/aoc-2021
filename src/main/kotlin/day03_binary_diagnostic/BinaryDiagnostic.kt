package day03_binary_diagnostic

fun main() {
    util.solve(3687446, ::partOne)
    util.solve(4406844, ::partTwo)
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

fun partTwo(input: String): Long {
    val lines = input.lines()
    var oxygen = lines
    for (i in 0..10000) {
        if (oxygen.size == 1) break
        val bitFreq = oxygen.fold(0) { n, line ->
            n + if (line[i] == '1') 1 else -1
        }
        val bit = if (bitFreq >= 0) '1' else '0'
        oxygen = oxygen.filter {
            it[i] == bit
        }
    }
    var co2 = lines
    for (i in 0..10000) {
        if (co2.size == 1) break
        val bitFreq = co2.fold(0) { n, line ->
            n + if (line[i] == '1') 1 else -1
        }
        val bit = if (bitFreq < 0) '1' else '0'
        co2 = co2.filter {
            it[i] == bit
        }
    }
    return oxygen.first().toLong(2) * co2.first().toLong(2)
}
