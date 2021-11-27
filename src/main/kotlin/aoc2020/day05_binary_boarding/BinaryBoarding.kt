package aoc2020.day05_binary_boarding

fun main() {
    util.solve(842, ::partOne)
    util.solve(617, ::partTwo)
}

private val IntRange.size
    get() = last - start + 1

private fun IntRange.partition(c: Char) = when (c) {
    'F', 'L' -> start..(last - size / 2)
    'B', 'R' -> (start + size / 2)..last
    else -> throw IllegalArgumentException("Unknown '$c' partition")
}

fun seatId(pass: String): Int {
    val r = pass.subSequence(0, 7).fold(0..127, IntRange::partition)
    assert(r.size == 1)
    val s = pass.substring(7).fold(0..7, IntRange::partition)
    assert(s.size == 1)
    return r.first * 8 + s.first
}

fun partOne(input: String) =
    input
        .lines()
        .maxOf(::seatId)

fun partTwo(input: String): Int {
    val ids = input
        .lines()
        .map(::seatId)
        .toSortedSet()
    for (id in ids.first()..ids.last()) {
        if (!ids.contains(id)) {
            return id
        }
    }
    throw IllegalStateException("No empty seat found?!")
}
