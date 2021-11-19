package aoc2020.day01_report_repair

fun main() {
    util.solve(73371, ::partOne)
    util.solve(127642310, ::partTwo)
}

private const val TARGET = 2020

private fun List<Int>.findAddends(total: Int): Pair<Int, Int>? {
    val set = hashSetOf<Int>()
    this.forEach {
        if (it >= total) return@forEach
        val rest = total - it
        if (set.contains(rest)) {
            return Pair(rest, it)
        }
        set.add(it)
    }
    return null
}

fun partOne(input: String): Int = input
    .lines()
    .map(Integer::parseInt)
    .findAddends(TARGET)
    ?.let { (a, b) -> a * b }
    ?: throw IllegalStateException("No pair found?!")

fun partTwo(input: String): Int {
    val items = input
        .lines()
        .map(Integer::parseInt)
    items.forEachIndexed { i, it ->
        items.subList(0, i)
            .findAddends(TARGET - it)
            ?.let { (a, b) -> return a * b * it }
    }
    throw IllegalStateException("No pair found?!")
}
