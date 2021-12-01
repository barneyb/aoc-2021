package day01_sonar_sweep

fun main() {
    util.solve(1616, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) =
    input
        .lines()
        .asSequence()
        .map { it.toInt() }
        .fold(Pair(0, Int.MAX_VALUE)) { (n, prev), it ->
            Pair(if (it > prev) n + 1 else n, it)
        }
        .first

fun partTwo(input: String) = input.trim().length
