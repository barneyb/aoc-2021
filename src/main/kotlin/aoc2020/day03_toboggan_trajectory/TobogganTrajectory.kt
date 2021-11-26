package aoc2020.day03_toboggan_trajectory

fun main() {
    util.solve(176, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) =
    input.lines().foldIndexed(0) { idx, agg, line ->
        if (line[idx * 3 % line.length] == '#') agg + 1
        else agg
    }

fun partTwo(input: String) = input.trim().length
