package aoc2020.day03_toboggan_trajectory

fun main() {
    util.solve(176, ::partOne)
    util.solve(5872458240, ::partTwo)
}

fun partOne(input: String) =
    input.lines().foldIndexed(0) { row, agg, line ->
        if (line[row * 3 % line.length] == '#') agg + 1
        else agg
    }

fun partTwo(input: String) =
    input.lines().foldIndexed(LongArray(5)) { row, agg, line ->
        if (line[row * 1 % line.length] == '#') ++agg[0]
        if (line[row * 3 % line.length] == '#') ++agg[1]
        if (line[row * 5 % line.length] == '#') ++agg[2]
        if (line[row * 7 % line.length] == '#') ++agg[3]
        if (row % 2 == 0) {
            if (line[(row / 2) % line.length] == '#') ++agg[4]
        }
        agg
    }.reduce(Long::times)
